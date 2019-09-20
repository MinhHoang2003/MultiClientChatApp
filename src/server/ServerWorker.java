/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.Command;
import static client.Command.RESPONSE;
import static client.Command.SEND;
import client.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
                this.clientSocket.close();
                handlerOffline();
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
                    send(Command.LOGOFF, "", this.acount.getUserName());
                    clientSocket.close();
                }
                case REGISTER: {
                    handlerRegister(message);
                    break;
                }
                case SEND:
                    handlerSendMessage(message);
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
        String password = message.getBody();
        int index = server.getAccountManager().logingAccount(user, password);
        if (index >= 0) {
            //anounce change status and change status in account manager
            this.acount = server.getAccountManager().getAccount(index);
            String msg = "ok login\n";
            Message response = new Message(Command.RESPONSE, msg, "System", this.getAcount().getUserName());
            this.acount.setStatus(AccountStatus.ONLINE);
            this.server.addWorker(this);
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
        String password = message.getBody();
        String msg = "";
        if (server.getAccountManager().registerAccount(user, password)) {
            msg = "register successfully with username " + user + "\n";
        } else {
            msg = "register fail your user name has been taken or password length invalid\n";
        }
        send(Command.RESPONSE, msg, Server.SYSTEM);
    }

    public void send(Command cmd, String msg, String from) throws IOException {
        Message message = null;
        if (cmd == RESPONSE) {
            message = new Message(cmd, msg, from, this.acount.getUserName());
        } else {
            message = new Message(cmd, msg, from, "");
        }

        this.outputStream.writeObject(message);
    }

    private void handlerOffline() throws IOException {
        this.acount.setStatus(AccountStatus.OFFLINE);
        server.removeWorker(this);
        String offlineStatus = this.acount.getUserName() + " has offlined\n";
        this.acount = null;
        System.out.println(offlineStatus);
    }

    private void handlerJoinRoom(Message message) throws IOException {
        if (this.acount == null) {
            send(Command.RESPONSE, "You must register to join a room\n", Server.SYSTEM);
            return;
        }
        String[] tokens = message.getBody().split(" ", 2);
        if (tokens.length == 1) {
            String roomName = tokens[0];
            Room room = server.getRoomManager().getRoomByName(roomName);
            if (room != null && room.getStatus() == RoomStatus.PUBLIC) {
                room.addWorkerMember(this);
                room.sendMessageToRoomate(this.acount.getUserName(), " has join the room\n");
                send(RESPONSE, "you has join room " + roomName + "\n", Server.SYSTEM);
            } else {
                send(RESPONSE, roomName + " is a private room, you must input the password\n", Server.SYSTEM);
            }
        } else if (tokens.length == 2) {
            String roomName = tokens[0];
            String pass = tokens[1];
            Room room = server.getRoomManager().getRoomByName(roomName);
            if (room != null && room.getPassword().equals(pass)) {
                room.addWorkerMember(this);
                room.sendMessageToRoomate(this.acount.getUserName(), " has join the room\n");
                send(RESPONSE, "you has join room " + roomName + "\n", Server.SYSTEM);
            } else {
                send(RESPONSE, "Wrong password in to " + roomName + "\n", Server.SYSTEM);
            }
        }
    }

    private void handlerSendMessage(Message message) throws IOException {
        String roomName = message.getReceiver();
        String msg = message.getBody();
        if (roomName.equals("General")) {
            for (ServerWorker worker : server.getWorkers()) {
                if (!worker.getAcount().getUserName().equals(this.acount.getUserName())) {
                    worker.send(message.getCmd(), msg, this.getAcount().getUserName());
                    System.out.println(this.acount.getUserName() + " send General: " + msg + "\n");
                }
            }
        } else {
            Room room = server.getRoomManager().getRoomByName(roomName);
            if (room != null) {
                room.sendMessageToRoomate(this.acount.getUserName(), msg);
            }
        }
    }

}
