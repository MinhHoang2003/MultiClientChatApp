/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import client.controller.Client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dohongquan
 */
public class UDPVoiceCall extends Thread {

    private DatagramSocket dataReceive;
    private DatagramSocket dataSend;
    private byte[] byte_read = new byte[512];

    public UDPVoiceCall(DatagramSocket dataReceive, DatagramSocket dataSend) {
        this.dataReceive = dataReceive;
        this.dataSend = dataSend;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket DpCome = new DatagramPacket(byte_read, byte_read.length);
                dataReceive.receive(DpCome);
                System.out.println(new String(byte_read));
                DatagramPacket DpSend = new DatagramPacket(byte_read, byte_read.length, InetAddress.getByName(Client.serverIP), 12346);
                dataSend.send(DpSend);
                byte_read = new byte[512];
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
