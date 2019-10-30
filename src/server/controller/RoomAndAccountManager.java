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

    private Map<String, ArrayList<String>> roomAndAccount;

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
        if (roomAndAccount.get(user) == null) {
            ArrayList<String> roomNames = new ArrayList<>();
            roomNames.add(roomName);
            roomAndAccount.put(user, roomNames);
        } else {
            roomAndAccount.get(user).add(roomName);
        }
    }

    public void removeRelationship(String user, String room) {
        Iterator it = roomAndAccount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ArrayList<String>> item = (Map.Entry<String, ArrayList<String>>) it.next();
            //it.remove() will delete the item from the map
            if (item.getKey().equals(user)) {
                ArrayList<String> rooms = item.getValue();
                Iterator<String> iterator = rooms.iterator();
                while (iterator.hasNext()) {
                    String language = iterator.next();
                    if (language.equals(room)) {
                        iterator.remove();
                    }
                }
            }
        }
    }
    

    public List<String> getRoomsByUser(String user) {
        return roomAndAccount.get(user);
    }
}
