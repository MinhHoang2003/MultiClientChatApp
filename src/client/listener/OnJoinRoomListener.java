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
public interface OnJoinRoomListener {

    void onJoinRoomSuccessful(String msg);

    void onJoinRoomFail(String msg);
}
