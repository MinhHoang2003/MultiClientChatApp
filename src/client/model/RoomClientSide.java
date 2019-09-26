/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model;

import java.io.Serializable;
import server.model.RoomStatus;

/**
 *
 * @author hoang
 */
public class RoomClientSide implements Serializable{
    private String name;
    private RoomStatus roomStatus; 

    public RoomClientSide(String name, RoomStatus roomStatus) {
        this.name = name;
        this.roomStatus = roomStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }
    
    
    
}
