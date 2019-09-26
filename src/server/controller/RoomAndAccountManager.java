/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class RoomAndAccountManager {

    private static RoomAndAccountManager roomAndAccountManager;
    private List<String> roomIds;
    private List<String> userNameMembers;

    public RoomAndAccountManager() {
        roomIds = new ArrayList<>();
        roomIds.add("General");
        userNameMembers = new ArrayList<>();
    }

    public RoomAndAccountManager(List<String> roomIds, List<String> userNameMembers) {
        this.roomIds = roomIds;
        this.userNameMembers = userNameMembers;
    }

    public static RoomAndAccountManager getInstance() {
        if (roomAndAccountManager == null) {
            roomAndAccountManager = new RoomAndAccountManager();
        }
        return roomAndAccountManager;
    }

    public List<String> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(List<String> roomIds) {
        this.roomIds = roomIds;
    }

    public List<String> getUserNameMembers() {
        return userNameMembers;
    }

    public void setUserNameMembers(List<String> userNameMembers) {
        this.userNameMembers = userNameMembers;
    }

    public boolean checkMember(String member, String room) {
        for (String roomName : roomIds) {
            if (roomName.equals(room)) {
                return true;
            }
        }
        return false;
    }

}
