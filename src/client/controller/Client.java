/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import client.model.Message;
import client.listener.MessageListener;
import client.listener.OnGetCallListener;
import client.listener.OnCreateRoomListener;
import client.listener.OnDeleteRoomListener;
import client.listener.OnGetFileListener;
import client.listener.OnGetHistoryListener;
import client.listener.OnGetRoomsListener;
import client.listener.OnInviteFriendListener;
import client.listener.OnJoinRoomListener;
import client.listener.OnLeaveRoomListener;
import client.listener.OnRespondVoiceCall;
import client.listener.UserStatusListener;
import client.view.LoginUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.listener.RoomMemmberListener;
import client.model.FileInfo;
import client.model.RoomClientSide;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import server.controller.Server;
import server.controller.ServerWorker;
import server.controller.UDPVoiceCall;

/**
 *
 * @author hoang
 */
public class Client {

    public static boolean connectionStatus = false;
    private final String serverName;
    private final int serverPort;
    public static String serverIP = "192.168.1.52";
    private Socket socket;
    private ObjectOutputStream serverOut;
    private ObjectInputStream serverIn;
    private String userName;
    // udp
    private Sender sender;
    private Receiver receiver;
    private String toIP;
    public static boolean flag = true;

    //message listener
    private final List<MessageListener> messageListeners = new ArrayList<>();
    private final List<UserStatusListener> userStatusListeners = new ArrayList<>();
    private final List<RoomMemmberListener> roomMemmberCallbacks = new ArrayList<>();
    private final List<OnGetRoomsListener> onGetRoomsListeners = new ArrayList<>();
    private final List<OnJoinRoomListener> onJoinRoomListeners = new ArrayList<>();
    private final List<OnLeaveRoomListener> onLeaveRoomListeners = new ArrayList<>();
    private final List<OnGetHistoryListener> onGetHistoryListeners = new ArrayList<>();
    private final List<OnGetFileListener> onGetFileListeners = new ArrayList<>();
    private final List<OnGetCallListener> onGetCallListeners = new ArrayList<>();
    private OnCreateRoomListener.OnCreateRoomResult onCreateRoomResult;
    private OnInviteFriendListener onInviteFriendListener;
    private List<OnDeleteRoomListener> onDeleteRoomListeners = new ArrayList<>();
    private OnRespondVoiceCall onRespondVoiceCall;

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

    public void addMessageListener(MessageListener listener) {
        this.messageListeners.add(listener);
    }

    public void addUserStatusListener(UserStatusListener listener) {
        this.userStatusListeners.add(listener);
    }

    public void addRoomMemmberCallback(RoomMemmberListener roomMemmberCallback) {
        this.roomMemmberCallbacks.add(roomMemmberCallback);
    }

    public void addOnGetHistoryListener(OnGetHistoryListener listener) {
        this.onGetHistoryListeners.add(listener);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addOnGetRoomsListener(OnGetRoomsListener onGetRoomsListener) {
        this.onGetRoomsListeners.add(onGetRoomsListener);
    }

    public void addOnJoinRoomListener(OnJoinRoomListener onJoinRoomListener) {
        this.onJoinRoomListeners.add(onJoinRoomListener);
    }

    public void addOnLeaveRoomListener(OnLeaveRoomListener listener) {
        this.onLeaveRoomListeners.add(listener);
    }

    public void addOnGetFileListener(OnGetFileListener listener) {
        this.onGetFileListeners.add(listener);
    }

    public void addOnGetCallListener(OnGetCallListener listener) {
        this.onGetCallListeners.add(listener);
    }

    public void setOnCreateRoomResult(OnCreateRoomListener.OnCreateRoomResult onCreateRoomResult) {
        this.onCreateRoomResult = onCreateRoomResult;
    }

    public void setOnInviteFriendListener(OnInviteFriendListener onInviteFriendListener) {
        this.onInviteFriendListener = onInviteFriendListener;
    }

    public void addOnDeleteRoomListener(OnDeleteRoomListener listener) {
        this.onDeleteRoomListeners.add(listener);
    }

    public void setOnRespondVoiceCall(OnRespondVoiceCall listener) {
        this.onRespondVoiceCall = listener;
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
        }
        return false;
    }

    public void startMessageReader() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    readMessageLoop();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SocketException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }

    public void readMessageLoop() throws ClassNotFoundException, SocketException {
        Message message;
        try {
            while (true) {
                message = (Message) serverIn.readObject();

                if (message != null) {
                    Command cmd = message.getCmd();
                    switch (cmd) {
                        case SEND: {
                            for (MessageListener listener : this.messageListeners) {
                                listener.onMessageListener(message);
                            }
                            break;
                        }
                        case RESPONSE: {
                            for (MessageListener listener : this.messageListeners) {
                                listener.onMessageListener(message);
                            }
                            break;
                        }
                        case LOGON: {
                            for (UserStatusListener listener : this.userStatusListeners) {
                                listener.onUserLogOn(message);
                            }
                            break;
                        }
                        case ROOM_MEMMBER:
                            for (RoomMemmberListener listener : roomMemmberCallbacks) {
                                listener.onRoomMemmberOnline(message);
                            }
                            break;
                        case LOGOFF: {
                            for (UserStatusListener listener : this.userStatusListeners) {
                                listener.onUserLogOff(message);
                            }
                            break;
                        }
                        case ROOM:
                            for (OnGetRoomsListener listener : this.onGetRoomsListeners) {
                                List<RoomClientSide> rooms = (List<RoomClientSide>) message.getBody();
                                System.out.println("geted rooms");
                                listener.onGetRooms(rooms);
                            }
                            break;
                        case JOIN:
                            for (OnJoinRoomListener listener : this.onJoinRoomListeners) {
                                String room = message.getFrom();
                                String status = (String) message.getBody();
                                System.out.println("Join room: " + room + " " + status);
                                if ("ok".equalsIgnoreCase(status)) {
                                    listener.onJoinRoomSuccessful(room);
                                } else {
                                    listener.onJoinRoomFail(status);
                                }
                            }
                            break;
                        case LEAVE:
                            for (OnLeaveRoomListener listener : this.onLeaveRoomListeners) {
                                listener.onLeaveRoom(message);
                            }
                            break;
                        case VOICECALL:
                            handlerIncomingCall(message);
                            break;
                        case HISTORY:
                            List<String> historys = (List<String>) message.getBody();
                            for (OnGetHistoryListener listener : onGetHistoryListeners) {
                                listener.onGetMessageHistorys(historys, message.getFrom());
                            }
                            break;
                        case FILE:
                            FileInfo file = (FileInfo) message.getBody();
                            System.out.println("file listener");
                            for (OnGetFileListener listener : onGetFileListeners) {
                                listener.onGetFile(file, message.getFrom());
                            }
                            break;
                        case ICON: {
                            for (MessageListener listener : this.messageListeners) {
                                listener.onMessageListener(message);
                            }
                            break;
                        }
                        case CREATE:
                            String result = (String) message.getBody();
                            if (result.equalsIgnoreCase("Done")) {
                                onCreateRoomResult.onCreateRoomSuccessful();
                            } else {
                                onCreateRoomResult.onCreateRoomFail();
                            }
                            break;
                        case INVITE:
                            String messBody = (String) message.getBody();
                            String room = message.getFrom();
                            if (messBody.startsWith("Fail")) {
                                onInviteFriendListener.onFailToInviateFriend(room, messBody);
                            } else {
                                onInviteFriendListener.onShowInviteMessage("", room, messBody);
                            }
                            break;
                        case DELETE:
                            String response = (String) message.getBody();
                            for (OnDeleteRoomListener listener : this.onDeleteRoomListeners) {
                                if (response.startsWith("success")) {
                                    listener.onDeleteSuccessful(message);
                                } else if (message.getReceiver().equals(userName)) {
                                    listener.onDeleteFail(message);
                                }
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
            System.out.println("get rooms-----------------------------------------");
            serverOut.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getChatHistory(String roomName) {
        try {
            Message message = new Message(Command.HISTORY, null, userName, roomName);
            System.out.println("Start get history form " + roomName);
            serverOut.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void joinRoom(String roomName, String password) throws IOException {
        Message<String> message = new Message<>(Command.JOIN, password, this.userName, roomName);
        serverOut.writeObject(message);
    }

    public boolean register(String user, String pass) throws IOException, ClassNotFoundException {
        Message msg = new Message(Command.REGISTER, pass, user, "System");
        this.serverOut.writeObject(msg);
        Message response = (Message) serverIn.readObject();
        System.out.println(response.getBody());
        if ("ok register\n".equalsIgnoreCase((String) response.getBody())) {
            System.out.println("register successful");
            return true;
        }
        return false;
    }

    public void leaveRoom(String roomName) throws IOException {
        Message<String> message = new Message<>(Command.LEAVE, "", this.getUserName(), roomName);
        this.serverOut.writeObject(message);
    }

    public FileInfo getFileInfo(String sourceFilePath) {
        FileInfo fileInfo = null;
        BufferedInputStream bis = null;
        try {
            File sourceFile = new File(sourceFilePath);
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            fileInfo = new FileInfo();
            byte[] fileBytes = new byte[(int) sourceFile.length()];
            // get file info
            bis.read(fileBytes, 0, fileBytes.length);
            fileInfo.setFilename(sourceFile.getName());
            fileInfo.setDataBytes(fileBytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fileInfo;
    }

    public boolean createFile(FileInfo fileInfo) {
        BufferedOutputStream bos = null;
        try {
            if (fileInfo != null) {
                String url = fileInfo.getDestinationDirectory() + "\\"
                        + fileInfo.getFilename();
                System.out.println("URL: " + url);
                File fileReceive = new File(url);
                bos = new BufferedOutputStream(
                        new FileOutputStream(fileReceive));
                // write file content
                bos.write(fileInfo.getDataBytes());
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    public void sendFile(String source, String roomName) {
        try {
            FileInfo file = getFileInfo(source);
            Message<FileInfo> message = new Message<>(Command.FILE, file, userName, roomName);
            serverOut.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void makeVoiceCall(String roomName, String toClient, String fromIP) {
        try {
            System.out.println(fromIP);
            Message<String> message = new Message(Command.VOICECALL, "MAKE " + toClient + " " + fromIP, userName, roomName);
            serverOut.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void acceptVoiceCall(String roomName, String toClient, String fromIP) {
        try {
            System.out.println(fromIP);
            Message<String> message = new Message(Command.VOICECALL, "ACCEPT " + toClient + " " + fromIP, userName, roomName);
            serverOut.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createRoom(String roomName, String roomType, String password) {
        try {
            String createRoomMessage = roomName + "|" + roomType + "|" + password;
            Message<String> createRoom = new Message<>(Command.CREATE, createRoomMessage, this.userName, Server.SYSTEM);

            serverOut.writeObject(createRoom);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void inviteFriendJoinRoom(String friendName, String roomName) {
        try {
            String inviteBody = "invite " + friendName;
            Message<String> invite = new Message<>(Command.INVITE, inviteBody, client.getUserName(), roomName);
            serverOut.writeObject(invite);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void responseInvite(String responseBody, String roomName) {
        try {
            Message<String> response = new Message<>(Command.INVITE, responseBody, this.getUserName(), roomName);
            serverOut.writeObject(response);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startVoiceChatThread() {
        this.receiver = new Receiver("localhost", 12345);
        this.sender = new Sender(toIP, 12346);
        receiver.start();
        sender.start();
    }

    public void stopVoiceChatThread() {
        Client.flag = false;
        if (receiver.isAlive()) {
            receiver.interrupt();
        }
        if(sender.isAlive()){
            sender.interrupt();
        }

    }

    public void deleteRoom(String roomName) {
        try {
            Message<String> deleteMessage = new Message<>(Command.DELETE, "", userName, roomName);
            serverOut.writeObject(deleteMessage);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StringBuilder data(byte[] a) {
        if (a == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }

    private void handlerIncomingCall(Message message) {
        String body = (String) message.getBody();
        String[] token = body.split(" ");
        if (token[0].equals("MAKE")) {
            this.toIP = token[1];
            for (OnGetCallListener listener : onGetCallListeners) {
                listener.onGetCall(message.getReceiver(), message.getFrom());
            }
        } else if (token[0].equals("ACCEPT")) {
            this.toIP = token[1];
            onRespondVoiceCall.acceptVoiceCall();
        } else {

        }
        System.out.println("call listener");
    }
}
