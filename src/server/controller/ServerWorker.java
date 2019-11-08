/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import server.model.RoomStatus;
import server.model.Account;
import server.model.Room;
import client.controller.Command;
import static client.controller.Command.SEND;
import client.listener.OnSendAudioListener;
import client.model.FileInfo;
import client.model.Message;
import client.model.RoomClientSide;
import server.dao.ImplRoomDAO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoang
 */
public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private Server server;
    private Account acount = null;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private OnSendAudioListener listener;

    ServerWorker(Server server, Socket clientsocket) {
        this.clientSocket = clientsocket;
        this.server = server;
    }

    public Account getAcount() {
        return acount;
    }

    @Override
    public void run() {
        try {
            handlerClientSocket();
        } catch (IOException ex) {
            try {
                System.out.println("close connection");
                handlerOffline(new Message(Command.LOGOFF, "", acount.getUserName(), "System"));
            } catch (IOException ex1) {
                Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handlerClientSocket() throws IOException, ClassNotFoundException {
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());

        Message message = null;
        Message messageCall = null;
        while (true) {

            System.out.println("Get message ...");

            message = (Message) inputStream.readObject();
            if (message != null) {
                Command cmd = message.getCmd();

                switch (cmd) {
                    case LOGIN: {
                        handlerLogin(outputStream, message);
                        break;
                    }
                    case JOIN: {
                        handlerJoinRoom(message);
                        break;
                    }
                    case QUIT: {
                        handlerOffline(message);
                        clientSocket.close();
                        break;
                    }
                    case REGISTER: {
                        handlerRegister(message);
                        break;
                    }
                    case SEND:
                        handlerSendMessage(message);
                        break;
                    case ROOM_MEMMBER:
                        handlerGetRoomMember(message);
                        break;
                    case ROOM:
                        handlerGetRooms(message);
                        break;
                    case LEAVE:
                        handlerLeaveRoom(message);
                        break;
                    case HISTORY:
                        handlerGetRoomHistory(message);
                        break;
                    case FILE:
                        handlerSendFile(message);
                        break;
                    case ICON:
                        handlerSendMessage(message);
                        break;
                    case CREATE:
                        handlerCreateRoom(message);
                        break;
                    case INVITE:
                        handlerInviteFriend(message);
                        break;
                    case VOICECALL:
                        handlerVoiceCall(message);
                        break;
                    case DELETE:
                        handlerDeleteRoom(message);
                        break;
                    default:
                        String msg = "unknown " + cmd + "\n";
                        System.out.println(msg);
                }
            }
        }
    }

    private void handlerLogin(ObjectOutputStream outputStream, Message message) throws IOException {
        String user = message.getFrom();
        String password = (String) message.getBody();
        int index = server.getAccountManager().logingAccount(user, password);
        if (index >= 0) {
            //anounce change status and change status in account manager
            this.acount = server.getAccountManager().getAccount(user, password);
            String msg = "ok login\n";
            Message response = new Message(Command.RESPONSE, msg, "System", this.getAcount().getUserName());
            this.acount.setStatus(AccountStatus.ONLINE);
            System.out.println("login " + this.acount.getUserName());
            outputStream.writeObject(response);
        } else {
            String msg = "error login\n";
            System.err.println(msg);
            Message response = new Message(Command.RESPONSE, msg, "System", "");
            outputStream.writeObject(response);
        }
    }

    private void handlerRegister(Message message) throws IOException {
        String user = message.getFrom();
        String password = (String) message.getBody();
        String msg = "";
        if (server.getAccountManager().registerAccount(user, password)) {
            msg = "ok register\n";
        } else {
            msg = "register fail your user name has been taken or password length invalid\n";
        }
        Message<String> response = new Message(Command.REGISTER, msg, Server.SYSTEM, user);
        send(response);
    }

    public void send(Message message) throws IOException {
        this.outputStream.writeObject(message);
    }

    public void sendDatagram(DatagramPacket dp) throws IOException {
        new DatagramSocket().send(dp);
    }

    public void response(Command cmd, String message) throws IOException {
        Message<String> response = new Message<>(cmd, message, Server.SYSTEM, this.getAcount().getUserName());
        this.outputStream.writeObject(response);
    }

    private void handlerOffline(Message message) throws IOException {
        String user = message.getFrom();
        List<String> rooms = new ArrayList<>();
        rooms.addAll(server.getRoomAndAccountManager().getRoomsByUser(user));
        Iterator<String> iterator = rooms.iterator();
        while (iterator.hasNext()) {
            String roomName = iterator.next();
            System.out.println(user + " quit room " + roomName);
            Room room = server.getRoomManager().getRoomByName(roomName);
            server.getRoomAndAccountManager().removeRelationship(user, roomName);
            server.removeWorker(this, roomName);
            if (room != null) {
                room.sendMessageToRoomate(Command.LEAVE, user, "");
            }
        }
        String offlineStatus = this.acount.getUserName() + " has offlined\n";
        this.acount = null;
        this.clientSocket.close();
        System.out.println(offlineStatus);
    }

    private void handlerJoinRoom(Message message) throws IOException {
        String roomName = message.getReceiver();
        String pass = (String) message.getBody();
        String user = message.getFrom();
        RoomAndAccountManager manager = server.getRoomAndAccountManager();
        String status = null;
        Room room = server.getRoomManager().getRoomByName(roomName);
        if (pass == null || pass.length() == 0) {
            if (room != null && room.getStatus() == RoomStatus.PUBLIC) {
                room.addWorkerMember(this);
                manager.addRelationship(user, roomName);
                room.sendMessageToRoomate(Command.LOGON, this.acount.getUserName(), " has join the room\n");
                status = "ok";
            } else {
                status = roomName + " is a private room, you must input the password\n";
            }
        } else {
            if (room != null && new ImplRoomDAO().checkPassword(roomName, pass)) {
                room.addWorkerMember(this);
                manager.addRelationship(user, roomName);
                room.sendMessageToRoomate(Command.LOGON, this.acount.getUserName(), " has join the room\n");
                status = "ok";
            } else {
                status = "Wrong password in to " + roomName + "\n";
            }
        }
        System.out.println(status);
        Message<String> msg = new Message<>(Command.JOIN, status, roomName, this.getAcount().getUserName());
        send(msg);

    }

    private void handlerSendMessage(Message message) throws IOException {
        String roomName = message.getReceiver();
        String msg = (String) message.getBody();
        Room room = server.getRoomManager().getRoomByName(roomName);
        room.sendMessageToRoomate(message.getCmd(), this.getAcount().getUserName(), msg);
    }

    private void handlerGetRoomMember(Message message) throws IOException {
        String roomName = message.getReceiver();
        StringBuilder builder = new StringBuilder();
        Room room = server.getRoomManager().getRoomByName(roomName);
        String line;
        if (room != null) {
            for (ServerWorker worker : room.getWorkers()) {
                line = worker.getAcount().getUserName() + " ";
                System.out.println("-------------- get room member");
                System.out.println(line);
                builder.append(line);
            }
            Message<String> reponse = new Message<>(message.getCmd(),
                    builder.toString(), Server.SYSTEM,
                    roomName);
            send(reponse);
        }
    }

    public void handlerGetRooms(Message message) throws IOException {
        List<RoomClientSide> roomCS = new ArrayList<>();
        List<Room> rooms = this.server.getRoomManager().getRooms();
        for (Room room : rooms) {
            roomCS.add(new RoomClientSide(room.getName(), room.getStatus()));
        }

        Message<List<RoomClientSide>> reponse = new Message<>(message.getCmd(),
                roomCS, Server.SYSTEM,
                this.getAcount().getUserName());
        send(reponse);
    }

    private void handlerLeaveRoom(Message message) throws IOException {
        String user = message.getFrom();
        String roomName = message.getReceiver();
        Room room = server.getRoomManager().getRoomByName(roomName);
        server.getRoomAndAccountManager().removeRelationship(user, roomName);
        server.removeWorker(this, roomName);
        if (room != null) {
            room.sendMessageToRoomate(Command.LEAVE, user, "");
        }
    }

    private void handlerGetRoomHistory(Message message) throws IOException {
        String roomName = message.getReceiver();
        String userName = message.getReceiver();
        Room room = server.getRoomManager().getRoomByName(roomName);
        if (room != null) {

            List<String> historys = room.getChatsHistory();
            Message<List<String>> reponse = new Message<>(Command.HISTORY, historys, roomName, userName);
            send(reponse);
        }
    }

    private void handlerSendFile(Message message) throws IOException {
        System.out.println("get a file from client");
        String roomName = message.getReceiver();
        String user = message.getFrom();
        Room room = server.getRoomManager().getRoomByName(roomName);
        if (room != null) {
            room.sendFileToRoom((FileInfo) message.getBody(), message.getFrom());
        }
    }

    private void startRinging(Message message) throws IOException {
        String roomName = message.getReceiver();
        String body = (String) message.getBody();
        String toUser = body.split(" ")[1]; String fromIP = body.split(" ")[2];
        String fromUser = message.getFrom();
        Room room = server.getRoomManager().getRoomByName(roomName);
        if (room != null) {
            System.out.println(fromIP);
            room.setStatusRinging(toUser, fromUser, fromIP);
        }
    }

    public void setListener(OnSendAudioListener listener) {
        this.listener = listener;
    }

    private void handlerCreateRoom(Message message) throws IOException {
        String creatRoomMessage = (String) message.getBody();
        String[] tokens = creatRoomMessage.split("\\|", 3);
        Message<String> response = null;
        if (tokens.length >= 2) {
            String roomName = tokens[0];
            String roomType = tokens[1];
            String password = null;
            Room room = server.getRoomManager().getRoomByName(roomName);
            if (room != null) {
                response = new Message<>(Command.CREATE, "Fail", Server.SYSTEM, message.getFrom());
            } else {
                if (roomType.equalsIgnoreCase("Public")) {
                    room = new Room(roomName, RoomStatus.PUBLIC);
                } else {
                    password = tokens[2];
                    room = new Room(roomName, RoomStatus.PRIVATE);
                    room.setPassword(password);
                }
                server.getRoomManager().createNewRoom(room, message.getFrom());
                response = new Message<>(Command.CREATE, "Done", Server.SYSTEM, message.getFrom());
            }
        }
        send(response);
    }

    private void handlerInviteFriend(Message message) throws IOException {
        String body = (String) message.getBody();
        String[] tokens = body.split(" ", 2);
        String command = tokens[0];
        String friendName = tokens[1];
        /* 4 types of command : 
        * invite : invite friend to room
        * fail : fail to invite -> not online, not exist name
        * accept : accept invite to room
        * refuse : refuse this invite
         */
        String roomName = message.getReceiver();
        String from = message.getFrom();
        switch (command) {
            case "invite":
                inviteFriend(roomName, from, friendName);
                break;
            case "accept":
                acceptInviter(roomName, from, friendName);
                break;
        }

    }

    private void inviteFriend(String roomName, String from, String friendName) throws IOException {
        if (!server.getAccountManager().isValidUserName(friendName)) {
            Message<String> response = new Message<>(Command.INVITE, "Fail : Account not exist", Server.SYSTEM, from);
            send(response);
            return;
        }
        String messBody = from + " invite you join :" + roomName;
        Message<String> inviteMessage = new Message<>(Command.INVITE, messBody, roomName, friendName);
        List<String> roomsHaveFriend = server.getRoomAndAccountManager().getRoomsByUser(friendName);
        if (roomsHaveFriend != null && roomsHaveFriend.size() > 0) {
            Room room = server.getRoomManager().getRoomByName(roomsHaveFriend.get(0));
            if (room != null) {
                ServerWorker friend = room.getWorkerByName(friendName);
                if (friend != null) {
                    friend.send(inviteMessage);
                }
            }
        } else {
            Message<String> response = new Message<>(Command.INVITE, "Fail : Your friend not online", Server.SYSTEM, from);
            send(response);
        }
    }

    private void acceptInviter(String roomName, String from, String inviter) throws IOException {
        Room room = server.getRoomManager().getRoomByName(roomName);
        if (room != null) {
            Message<String> joinRoom = new Message<>(Command.JOIN, room.getPassword(), from, roomName);
            handlerJoinRoom(joinRoom);
        }
    }

    private void handlerDeleteRoom(Message message) throws IOException {
        String user = message.getFrom();
        Message<String> deleteResponse = null;
        String roomName = message.getReceiver();
        RoomManager manager = server.getRoomManager();
        Room room = manager.getRoomByName(roomName);
        if (room != null && manager.isOwner(roomName, user)) {
            room.sendMessageToRoomate(Command.DELETE, roomName, "success");
            deleteResponse = new Message<>(Command.DELETE, "success", roomName, user);
            send(deleteResponse);
            manager.deleteRoom(roomName);
        } else {
            deleteResponse = new Message<>(Command.DELETE, "fail", roomName, user);
            send(deleteResponse);
        }
    }

    private void handlerVoiceCall(Message message) throws IOException {
        startRinging(message);
        String body = (String) message.getBody();
        if (body.startsWith("MAKE")) {
            this.startRinging(message);
        } //else if () {
//            
//        } else {
//            
//        }
//        listener.startUDPThread(messageCall);
    }
}
