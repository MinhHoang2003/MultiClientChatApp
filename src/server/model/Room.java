/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import client.controller.Command;
import client.model.FileInfo;
import client.model.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ServerWorker;
import server.dao.ImplRoomDAO;

/**
 *
 * @author hoang
 */
public class Room {

    private String name;
    private String password;
    private RoomStatus status;
    private List<String> chatsHistory;
    private ArrayList<ServerWorker> workers;
    private ImplRoomDAO implRoomDAO;

    public Room(String name, RoomStatus roomStatus) {
        this.name = name;
        this.status = roomStatus;
        workers = new ArrayList<>();
        chatsHistory = new ArrayList<>();
        implRoomDAO = new ImplRoomDAO();
    }

    public void addWorkerMember(ServerWorker member) {
        workers.add(member);
        System.out.println("Online in room " + name + " " + workers.size());
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<ServerWorker> getWorkers() {
        return workers;
    }

    public List<String> getChatsHistory() {
        return chatsHistory;
    }

    public void setChatsHistory(List<String> chatsHistory) {
        this.chatsHistory = chatsHistory;
    }

    public void sendMessageToRoomate(Command command, String from, String msg) throws IOException {
        String messageContent = from + " :" + msg;
        if (command == Command.SEND) {
            // save mess to DB
            boolean c = implRoomDAO.insertMess(this.name, from, msg);
            this.chatsHistory.add(messageContent); 
        }
        for (ServerWorker worker : workers) {
            if (!worker.getAcount().getUserName().equals(from)) { // send mess 
                Message<String> message = new Message(command, msg, from, this.getName());
                worker.send(message);
            }
        }
    }

    public void sendFileToRoom(FileInfo file, String from) throws IOException {
        for (ServerWorker worker : workers) {
            Message<FileInfo> message = new Message(Command.FILE, file, from, this.getName());
            worker.send(message);
        }

    }

}
