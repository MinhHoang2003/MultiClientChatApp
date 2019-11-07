/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
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
    private DatagramSocket dataSendMe;
    private byte[] byte_read = new byte [512];

    public UDPVoiceCall() {
        try {
            this.dataReceive = new DatagramSocket(12345);
            this.dataSend = new DatagramSocket();
            this.dataSendMe = new DatagramSocket();
            System.out.println("go constructor");
        } catch (SocketException ex) {
            Logger.getLogger(UDPVoiceCall.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        System.out.println("go run");
        while (true) {
            System.out.println("go loop");
            try {
                DatagramPacket DpCome = new DatagramPacket(byte_read, byte_read.length);
                dataReceive.receive(DpCome);
                System.out.println(data(byte_read));
                DatagramPacket DpSend = new DatagramPacket(byte_read, byte_read.length, InetAddress.getByName("192.168.1.83"), 12346);
                dataSend.send(DpSend);
                byte_read = new byte [512];
                DatagramPacket DpSendMyself = new DatagramPacket(byte_read, byte_read.length, InetAddress.getByName("localhost"), 12346);
                dataSendMe.send(DpSendMyself);
                byte_read = new byte [512];
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public StringBuilder data(byte[] a) 
    { 
        if (a == null) 
            return null; 
        StringBuilder ret = new StringBuilder(); 
        int i = 0; 
        while (a[i] != 0) 
        { 
            ret.append((char) a[i]); 
            i++; 
        } 
        return ret; 
    }
}
