/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import client.model.Message;
import client.listener.MessageListener;
import client.listener.OnGetRoomsListener;
import client.listener.UserStatusListener;
import client.view.LoginUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.listener.RoomMemmberListener;
import client.model.RoomClientSide;
import java.util.List;
import server.controller.Server;

/**
 *
 * @author hoang
 */
public class Client {

    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private ObjectOutputStream serverOut;
    private ObjectInputStream serverIn;
    private String userName;

    //message listener
    private MessageListener messageListener;
    private UserStatusListener userStatusListener;
    private RoomMemmberListener roomMemmberCallback;
    private OnGetRoomsListener onGetRoomsListener;

    private Client(LoginUI clientView, String serverName, int port) {
        this.serverName = serverName;
        this.serverPort = port;
    }

    private static Client client;

    public static Client getClient(LoginUI clientView, String serverName, int port) {
        if (client == null) {
            client = new Client(clientView, serverName, port);
        }
        return client;
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    public void setUserStatusListener(UserStatusListener listener) {
        this.userStatusListener = listener;
    }

    public void setRoomMemmberCallback(RoomMemmberListener roomMemmberCallback) {
        this.roomMemmberCallback = roomMemmberCallback;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setOnGetRoomsListener(OnGetRoomsListener onGetRoomsListener) {
        this.onGetRoomsListener = onGetRoomsListener;
    }
    
    public boolean connection() {
        try {
            this.socket = new Socket(serverName, serverPort);
            this.serverOut = new ObjectOutputStream(socket.getOutputStream());
            this.serverIn = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean login(String user, String pass) throws IOException, ClassNotFoundException {
        Message msg = new Message(Command.LOGIN, pass, user, "System");
        serverOut.writeObject(msg);
        Message response = (Message) serverIn.readObject();
        System.out.println(response.getBody());

        if ("ok login\n".equalsIgnoreCase((String) response.getBody())) {
            System.out.println("start read message");
            client.setUserName(user);
            return true;
        } else {
            return false;
        }
    }

    public void startMessageReader() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    readMessageLoop();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }

    public void readMessageLoop() throws ClassNotFoundException {
        Message message;
        try {
            while (true) {
                message = (Message) serverIn.readObject();
                Command cmd = message.getCmd();
                switch (cmd) {
                    case SEND: {
                        if (messageListener != null) {
                            System.out.println("Read object .........");
                            messageListener.onMessageListener(message);
                        }
                        break;
                    }
                    case RESPONSE: {
                        if (messageListener != null) {
                            messageListener.onMessageListener(message);
                        }
                        break;
                    }
                    case LOGON: {
                        if (userStatusListener != null) {
                            userStatusListener.onUserLogOn(message);
                        }
                        break;
                    }
                    case ROOM_MEMMBER:
                        if (roomMemmberCallback != null) {
                            roomMemmberCallback.onRoomMemmberOnline(message);
                        }
                        break;
                    case LOGOFF: {
                        if (userStatusListener != null) {
                            userStatusListener.onUserLogOff(message);
                        }
                        break;
                    }
                    case ROOM:
                        if (onGetRoomsListener != null) {
                            List<RoomClientSide> rooms = (List<RoomClientSide>) message.getBody();
                            onGetRoomsListener.onGetRooms(rooms);
                        }
                        break;
                }
            }
        } catch (IOException ex) {
            try {
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    public void sendMessage(Command cmd, String receiver, String msg) {
        try {
            Message message = new Message(cmd, msg, userName, receiver);
            System.out.println("Start send object message...");
            serverOut.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getRoomsClientSide() {
        try {
            Message message = new Message(Command.ROOM, null, userName, Server.SYSTEM);
            serverOut.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
