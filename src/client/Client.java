/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

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

    public Client(String serverName, int port) {
        this.serverName = serverName;
        this.serverPort = port;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8188);
        if (client.connection() == false) {
            System.err.println("Connection fail");
        } else {
            System.out.println("Connection successful");
            if (client.login("guest", "guest")) {
                System.out.println("Login successful");
                client.setUserName("guest");
                client.sendMessage("develop", "Hello world!");
            }
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private boolean connection() {
        try {
            this.socket = new Socket(serverName, serverPort);
            this.serverOut = new ObjectOutputStream( socket.getOutputStream());
            this.serverIn = new ObjectInputStream(socket.getInputStream());
            this.bufferedReader = new BufferedReader(new InputStreamReader(serverIn));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean login(String user, String pass) throws IOException {
        String cmd = "login " + user + " " + pass + "\n";
        serverOut.write(cmd.getBytes());
        String response = bufferedReader.readLine();
        System.out.println(response);

        if ("ok login".equalsIgnoreCase(response)) {
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }

    private void startMessageReader() {
        Thread t = new Thread() {
            @Override
            public void run() {
                readMessageLoop();
            }
        };
        t.start();
    }

    private void readMessageLoop() {
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(" ", 3);
                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];
                    if ("online".equalsIgnoreCase(cmd)) {
                        handlerOnline(tokens);
                    } else if ("send".equalsIgnoreCase(cmd)) {
                        handlerMessage(tokens);
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

    private void handlerOnline(String[] tokens) {
        String user = tokens[1];

    }

    private void sendMessage(String receiver, String msg) {
        try {
            Command cmd = Command.JOIN;
            Message message = new Message(cmd, msg, userName, receiver);
            System.out.println("Start send object message...");
            serverOut.writeObject(message);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handlerMessage(String[] tokens) {
        String user = tokens[1];
        String body = tokens[2];
        System.out.println(user + " : " + body + "\n");
    }
}
