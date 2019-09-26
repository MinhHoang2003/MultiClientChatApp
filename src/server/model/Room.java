/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import client.controller.Command;
import client.model.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ServerWorker;

/**
 *
 * @author hoang
 */
public class Room {

    private String name;
    private String password;
    private RoomStatus status;
    private ArrayList<ServerWorker> workers;

    public Room(String name, RoomStatus roomStatus) {
        this.name = name;
        this.status = roomStatus;
        workers = new ArrayList<>();
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

    public void sendMessageToRoomate(Command command, String userName, String msg) throws IOException {
        for (ServerWorker worker : workers) {
            if (!worker.getAcount().getUserName().equals(userName)) {
                Message<String> message = new Message(command, msg, userName, "");
                worker.send(message);
            }
        }
    }

}
