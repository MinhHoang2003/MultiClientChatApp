/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import client.model.RoomClientSide;
import java.util.List;
import server.model.MessInRoom;

/**
 *
 * @author hoain
 */
public interface RoomDAO {
    boolean insertMess(String roomName, String username, String content);
    List<MessInRoom> getMessByRoomName(String roomName);
    // 31/10/2019
    List<RoomClientSide> getRoomClient();
    RoomClientSide getRoomByName(String roomName);
    boolean checkExitsRoom(String roomName); // proc getRoomByName
    boolean checkOwner(String roomName, String owner); // proc getOwnerByRoomName
    boolean deleteRoomByName(String roomName);
    boolean addRoom(String roomName, String password, int status, String owner);
    boolean checkPassword(String roomName, String password);
    // check join room
}
