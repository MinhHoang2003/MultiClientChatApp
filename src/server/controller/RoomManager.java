/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import client.model.RoomClientSide;
import dao.ImplRoomDAO;
import dao.RoomDAO;
import server.model.RoomStatus;
import server.model.Room;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class RoomManager {

    private static RoomManager roomManager;
    private ArrayList<Room> rooms;
    private RoomDAO roomDAO;

    public static RoomManager getInstance() {
        if (roomManager == null) {
            roomManager = new RoomManager();
        }
        return roomManager;
    }

    public RoomManager() {
        roomDAO = new ImplRoomDAO();
        rooms = new ArrayList<>();
        List<RoomClientSide> listRoom = roomDAO.getRoomClient();
        for (RoomClientSide l : listRoom) {
            rooms.add(new Room(l.getName(), l.getRoomStatus()));
        }

//        rooms.add(new Room("General", RoomStatus.PUBLIC));
//        rooms.add(new Room("develop", RoomStatus.PUBLIC));
//        Room room = new Room("test", RoomStatus.PRIVATE);
//        List<String> historys = new ArrayList<>();
//        historys.add("nam : hello");
//        room.setChatsHistory(historys);
//
//        room.setPassword("hello");
//        rooms.add(room);
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
        } else if (room.getStatus() == RoomStatus.PRIVATE && roomDAO.checkPassword(name, password)) {
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

    public void createNewRoom(Room room, String owner) {
        System.out.println("Create new room " + room.getName());
        if (room.getStatus() == RoomStatus.PRIVATE) {
            roomDAO.addRoom(room.getName(), room.getPassword(), 0, owner);
        } else {
            roomDAO.addRoom(room.getName(), room.getPassword(), 1, owner);
        }
        this.rooms.add(room);
    }
}
