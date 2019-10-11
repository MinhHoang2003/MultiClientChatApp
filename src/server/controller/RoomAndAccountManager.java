/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hoang
 */
public class RoomAndAccountManager {

    private static RoomAndAccountManager roomAndAccountManager;

    private Map<String, String> roomAndAccount;

    private RoomAndAccountManager() {
        roomAndAccount = new HashMap<>();
    }

    public static RoomAndAccountManager getInstance() {
        if (roomAndAccountManager == null) {
            roomAndAccountManager = new RoomAndAccountManager();
        }
        return roomAndAccountManager;
    }

    public void addRelationship(String user, String roomName) {
        roomAndAccount.put(user, roomName);
    }

    public void removeRelationship(String user, String room) {
        Iterator it = roomAndAccount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> item = (Map.Entry<String, String>) it.next();
            //it.remove() will delete the item from the map
            if (item.getValue().equals(room) && item.getKey().equals(user)) {
                it.remove();
            }
        }
    }

    public List<String> getRoomsByUser(String user) {
        List<String> rooms = new ArrayList<>();
        roomAndAccount.forEach((userName, roomName) -> {
            if (user.equals(userName)) {
                rooms.add(roomName);
            }
        });
        return rooms;
    }
}
