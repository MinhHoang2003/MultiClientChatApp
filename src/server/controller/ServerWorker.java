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
                default:
                    String msg = "unknown " + cmd + "\n";
                    System.out.println(msg);
            }
        }
        clientSocket.close();
    }

    private void handlerLogin(ObjectOutputStream outputStream, Message message) throws IOException {
        String user = message.getUserName();
        String password = (String) message.getBody();
        int index = server.getAccountManager().logingAccount(user, password);
        if (index >= 0) {
            //anounce change status and change status in account manager
            this.acount = server.getAccountManager().getAccount(index);
            String msg = "ok login\n";
            Message response = new Message(Command.RESPONSE, msg, "System", this.getAcount().getUserName());
            this.acount.setStatus(AccountStatus.ONLINE);
            this.server.addWorker(this, "General");
            System.out.println("login " + this.acount.getUserName());
            outputStream.writeObject(response);
            handlerSendMessage(new Message(Command.LOGON, "", this.getAcount().getUserName(), "General"));
        } else {
            String msg = "error login\n";
            System.err.println(msg);
            Message response = new Message(Command.RESPONSE, msg, "System", this.getAcount().getUserName());
            outputStream.writeObject(response);
        }
    }

    private void handlerRegister(Message message) throws IOException {
        String user = message.getUserName();
        String password = (String) message.getBody();
        String msg = "";
        if (server.getAccountManager().registerAccount(user, password)) {
            msg = "register successfully with username " + user + "\n";
        } else {
            msg = "register fail your user name has been taken or password length invalid\n";
        }
        response(Command.RESPONSE, msg);
    }

    public void send(Message message) throws IOException {
        this.outputStream.writeObject(message);
    }

    public void response(Command cmd, String message) throws IOException {
        Message<String> response = new Message<>(cmd, message, Server.SYSTEM, this.getAcount().getUserName());
        this.outputStream.writeObject(response);
    }

    private void handlerOffline(Message message) throws IOException {
        Room room = server.getRoomManager().getRoomByName(message.getReceiver());
        if (room != null) {
            room.sendMessageToRoomate(Command.LOGOFF, message.getUserName(), "");
        }
        server.removeWorker(this, "General");
        String offlineStatus = this.acount.getUserName() + " has offlined\n";
        this.acount = null;
        this.clientSocket.close();
        System.out.println(offlineStatus);
    }

    private void handlerJoinRoom(Message message) throws IOException {
        String msg = null;
        if (this.acount == null) {
            msg = "You must register to join a room\n";
            response(Command.RESPONSE, msg);
            return;
        }
        String line = (String) message.getBody();
        String[] tokens = line.split(" ", 2);
        if (tokens.length == 1) {
            String roomName = tokens[0];
            Room room = server.getRoomManager().getRoomByName(roomName);
            if (room != null && room.getStatus() == RoomStatus.PUBLIC) {
                room.addWorkerMember(this);
                room.sendMessageToRoomate(Command.RESPONSE, this.acount.getUserName(), " has join the room\n");
                msg = "you has join room " + roomName + "\n";
            } else {
                msg = roomName + " is a private room, you must input the password\n";
            }
        } else if (tokens.length == 2) {
            String roomName = tokens[0];
            String pass = tokens[1];
            Room room = server.getRoomManager().getRoomByName(roomName);
            if (room != null && room.getPassword().equals(pass)) {
                room.addWorkerMember(this);
                room.sendMessageToRoomate(Command.RESPONSE, this.acount.getUserName(), " has join the room\n");
                msg = "you has join room " + roomName + "\n";
            } else {
                msg = "Wrong password in to " + roomName + "\n";
            }
        }
        response(Command.RESPONSE, msg);
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
            response(Command.ROOM_MEMMBER, builder.toString());
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

}
