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
import client.model.FileInfo;
import client.model.Message;
import client.model.RoomClientSide;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
        while ((message = (Message) inputStream.readObject()) != null) {
            System.out.println("Get message ...");
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
                default:
                    String msg = "unknown " + cmd + "\n";
                    System.out.println(msg);
            }
        }
        clientSocket.close();
    }

    private void handlerLogin(ObjectOutputStream outputStream, Message message) throws IOException {
        String user = message.getFrom();
        String password = (String) message.getBody();
        int index = server.getAccountManager().logingAccount(user, password);
        if (index >= 0) {
            //anounce change status and change status in account manager
            this.acount = server.getAccountManager().getAccount(index);
            String msg = "ok login\n";
            Message response = new Message(Command.RESPONSE, msg, "System", this.getAcount().getUserName());
            this.acount.setStatus(AccountStatus.ONLINE);
            // this.server.addWorker(this, "General");
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
        System.out.println("halderRegister " + msg);
        Message<String> response = new Message(Command.REGISTER, msg, Server.SYSTEM, user);
        send(response);
    }

    public void send(Message message) throws IOException {
        this.outputStream.writeObject(message);
    }

    public void response(Command cmd, String message) throws IOException {
        Message<String> response = new Message<>(cmd, message, Server.SYSTEM, this.getAcount().getUserName());
        this.outputStream.writeObject(response);
    }

    private void handlerOffline(Message message) throws IOException {
        String user = message.getFrom();
        List<String> rooms = server.getRoomAndAccountManager().getRoomsByUser(user);
        for (String roomName : rooms) {
            Room room = server.getRoomManager().getRoomByName(roomName);
            if (room != null) {
                System.out.println("Quit room " + room.getName());
                server.removeWorker(this, roomName);
                message.setCmd(Command.LEAVE);
                handlerLeaveRoom(message);
//                room.sendMessageToRoomate(Command.LOGOFF, message.getFrom(), "");
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
            if (room != null && room.getPassword().equals(pass)) {
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
             Message<List<String>> reponse = new Message<>(Command.HISTORY,historys,roomName,userName);
             send(reponse);
        }
    }

    private void handlerSendFile(Message message) throws IOException {
        System.out.println("get a file from client");
        String roomName = message.getReceiver();
        String user = message.getFrom();
        Room room = server.getRoomManager().getRoomByName(roomName);
        if(room != null){
            room.sendFileToRoom((FileInfo) message.getBody(),message.getFrom());
        }
    }

}
