/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private InputStream inputStream;
    private OutputStream outputStream;

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
        }
    }

    private void handlerClientSocket() throws IOException {
        this.outputStream = clientSocket.getOutputStream();
        this.inputStream = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(" ", 3);
            String cmd = tokens[0];
            if (tokens != null && tokens.length > 0) {
                if ("quit".equalsIgnoreCase(cmd)) {
                    handlerOffline();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handlerLogin(outputStream, tokens);
                } else if ("register".equals(cmd)) {
                    handlerRegister(tokens);
                } else if ("join".equalsIgnoreCase(cmd)) {
                    handlerJoinRoom(tokens);
                } else if ("send".equalsIgnoreCase(cmd)) {
                    handlerSendMessage(tokens);
                } else {
                    String msg = "unknown " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        clientSocket.close();
    }

    private void handlerLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String user = tokens[1];
            String password = tokens[2];
            int index = server.getAccountManager().logingAccount(user, password);
            if (index >= 0) {
                //anounce change status and change status in account manager
                String msg = "ok login\n";

                this.acount = server.getAccountManager().getAccount(index);
                this.acount.setStatus(AccountStatus.ONLINE);
                this.server.addWorker(this);
                System.out.println("login " + this.acount.getUserName());
                outputStream.write(msg.getBytes());
            } else {
                String msg = "error login\n";
                System.err.println(msg);
                outputStream.write(msg.getBytes());
            }
        }
    }

    private void handlerRegister(String[] tokens) throws IOException {
        if (tokens.length == 3 && this.acount == null) {
            String user = tokens[1];
            String password = tokens[2];
            String msg = "";
            if (server.getAccountManager().registerAccount(user, password)) {
                msg = "register successfully with username " + user + "\n";
            } else {
                msg = "register fail your user name has been taken or password length invalid\n";
            }
            send(msg);
        }
    }

    public void send(String msg) throws IOException {
        this.outputStream.write(msg.getBytes());
    }

    private void handlerOffline() throws IOException {
        this.acount.setStatus(AccountStatus.OFFLINE);
        server.removeWorker(this);
        String offlineStatus = this.acount.getUserName() + " has offlined\n";
        this.acount = null;
        System.out.println(offlineStatus);
    }

    private void handlerJoinRoom(String[] tokens) throws IOException {
        if (this.acount == null) {
            send("You must register to join a room\n");
            return;
        }
        if (tokens.length == 2) {
            String roomName = tokens[1];
            Room room = server.getRoomManager().getRoomByName(roomName);
            if (room != null && room.getStatus() == RoomStatus.PUBLIC) {
                room.addWorkerMember(this);
                room.sendMessageToRoomate(this.acount.getUserName(), " has join the room\n");
                send("you has join room " + roomName + "\n");
            } else {
                send(roomName + " is a private room, you must input the password\n");
            }
        } else if (tokens.length > 2) {
            String roomName = tokens[1];
            String pass = tokens[2];
            Room room = server.getRoomManager().getRoomByName(roomName);
            if (room != null && room.getPassword().equals(pass)) {
                room.addWorkerMember(this);
                room.sendMessageToRoomate(this.acount.getUserName(), " has join the room\n");
                send("you has join room " + roomName + "\n");
            } else {
                send("Wrong password in to " + roomName + "\n");
            }
        }
    }

    private void handlerSendMessage(String[] tokens) throws IOException {
        String roomName = tokens[1];
        String msg = tokens[2];
        if (roomName.equals("General")) {
            for (ServerWorker worker : server.getWorkers()) {
                worker.send(this.acount.getUserName() + " send General: " + msg + "\n");
            }
        } else {
            Room room = server.getRoomManager().getRoomByName(roomName);
            if (room != null) {
                room.sendMessageToRoomate(this.acount.getUserName(), msg);
            }
        }
    }

}
