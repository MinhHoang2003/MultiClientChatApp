/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.listener;

/**
 *
 * @author hoang
 */
public interface OnCreateRoomListener {

    interface OnStartCreateRoom {

        void onStart(String roomName, String roomType, String pass);
    }

    interface OnCreateRoomResult {

        void onCreateRoomSuccessful();

        void onCreateRoomFail();
    }
}
