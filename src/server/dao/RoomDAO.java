/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.dao;

import java.util.List;
import server.model.MessInRoom;

/**
 *
 * @author hoain
 */
public interface RoomDAO {
    boolean insertMess(String roomName, String username, String content);
    List<MessInRoom> getMessByRoomName(String roomName);
}
