/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import static client.Command.JOIN;
import client.view.LoginUI;
import client.view.MainChatClientScreen;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

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
    private BufferedReader bufferedReader;
    private String userName;
    private LoginUI clientView;
    private MessageListener messageListener;
    private UserStatusListener userStatusListener;

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
    public void setUserStatusListener(UserStatusListener listener){
        this.userStatusListener = listener;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean connection() {
        try {
            this.socket = new Socket(serverName, serverPort);
            this.serverOut = new ObjectOutputStream(socket.getOutputStream());
            this.serverIn = new ObjectInputStream(socket.getInputStream());
            this.bufferedReader = new BufferedReader(new InputStreamReader(serverIn));
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

        if ("ok login\n".equalsIgnoreCase(response.getBody())) {
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
                    case LOGOFF: {
                        if (userStatusListener != null) {
                            userStatusListener.onUserLogOff(message);
                        }
                        break;

                    }
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

    public void handlerOnline(String[] tokens) {
        String user = tokens[1];

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

    public void handlerMessage(String[] tokens) {
        String user = tokens[1];
        String body = tokens[2];
        System.out.println(user + " : " + body + "\n");
    }
}
