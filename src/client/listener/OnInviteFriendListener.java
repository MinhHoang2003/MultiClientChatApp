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
public interface OnInviteFriendListener {

    interface OnStartInviteFriendListener {

        void onStart(String friend);
    }

    void onFailToInviateFriend(String roomName, String mess);

    void onShowInviteMessage(String inviter,String rommName,String mess);
}
