/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import server.model.RoomStatus;
import server.model.Room;
import java.util.ArrayList;

/**
 *
 * @author hoang
 */
public class RoomManager {

    private static RoomManager roomManager;
    private ArrayList<Room> rooms;

    public static RoomManager getInstance() {
        if (roomManager == null) {
            roomManager = new RoomManager();
        }
        return roomManager;
    }

    public RoomManager() {
        rooms = new ArrayList<>();
        rooms.add(new Room("General", RoomStatus.PUBLIC));
        rooms.add(new Room("develop", RoomStatus.PUBLIC));
        Room room = new Room("test", RoomStatus.PRIVATE);
        room.setPassword("hello");
        rooms.add(room);
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public Room getRoomByName(String name, String password) {
        Room room = getRoomByName(name);
        if (room.getStatus() == RoomStatus.PRIVATE && password == null) {
            return null;
        } else if (room.getStatus() == RoomStatus.PRIVATE && password.equals(room.getPassword())) {
            return room;
        }
        return null;
    }

    public Room getRoomByName(String name) {
        for (Room room : rooms) {
            if (room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

}
