/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model;

import client.controller.Command;
import java.io.Serializable;

/**
 *
 * @author hoang
 */
public class Message<T> implements Serializable {

    private Command cmd;
    private T body;
    private String userName;
    private String receiver;

    public Message(Command cmd, T body, String userName, String receiver) {
        this.cmd = cmd;
        this.body = body;
        this.userName = userName;
        this.receiver = receiver;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Command getCmd() {
        return cmd;
    }

    public void setCmd(Command cmd) {
        this.cmd = cmd;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

}
