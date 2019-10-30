/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

/**
 *
 * @author hoang
 */
public enum Command {
    SEND, RESPONSE,
    LOGIN, REGISTER, QUIT,
    LOGON, LOGOFF,
    VOICECALL,
    JOIN, ROOM_MEMMBER, ROOM, LEAVE, HISTORY, FILE, ICON, CREATE, INVITE,INVITE_ACCEPT,INVITE_REFUSE;
}
