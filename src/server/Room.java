/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoang
 */
public class Room {

    private String name;
    private String password;
    private RoomStatus status;
    private List<ServerWorker> workers;

    public Room(String name, RoomStatus roomStatus) {
        this.name = name;
        this.status = roomStatus;
        workers = new ArrayList<>();
    }

    public void addWorkerMember(ServerWorker member) {
        workers.add(member);
        System.out.println("Online in room " + name + " " + workers.size());
    }

    public void sendMessageInRoom(String msg) {
        for (ServerWorker worker : this.workers) {
            try {
                worker.send(msg);
            } catch (IOException ex) {
                Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    public void sendMessageToRoomate(String userName, String msg) throws IOException {
        for (ServerWorker worker : workers) {
            if (!worker.getAcount().getUserName().equals(userName)) {
                worker.send(userName + " :" + msg + "\n");
            }
        }
    }

}
