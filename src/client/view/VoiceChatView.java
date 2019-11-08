/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.controller.Client;
import client.listener.OnRespondVoiceCall;

/**
 *
 * @author hoang
 */
public class VoiceChatView extends javax.swing.JFrame implements OnRespondVoiceCall {

    private final Client client;
    private final String roomName;
    private final String toClient;
    private boolean isConnected = false;

    public VoiceChatView(String roomName, Client client, String toClient, boolean isConnected) {
        initComponents();
        this.roomName = roomName;
        this.toClient = toClient;
        this.room.setText(roomName);
        this.client = client;
        this.isConnected = isConnected;
        client.setOnRespondVoiceCall(this);
        room.setText(roomName);
        jLabel4.setText("Voice call to: " + toClient);
        statusWhenCalling.setText("Connecting...");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        if (this.isConnected) {
            statusWhenCalling.setText("Connected");
            client.startVoiceChatThread();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        stop = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        room = new javax.swing.JLabel();
        statusWhenCalling = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/group.png"))); // NOI18N

        stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/stopCalling.png"))); // NOI18N
        stop.setText("Stop");
        stop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stopMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel4.setText("Voice call to: ");

        room.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        room.setText("jLabel5");

        statusWhenCalling.setText("Connecting...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stop)
                            .addComponent(room)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(statusWhenCalling)))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(room)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(statusWhenCalling)
                .addGap(26, 26, 26)
                .addComponent(stop)
                .addGap(53, 53, 53))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopMouseClicked
        client.stopVoiceChatThread();
        this.dispose();
    }//GEN-LAST:event_stopMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel room;
    private javax.swing.JLabel statusWhenCalling;
    private javax.swing.JLabel stop;
    // End of variables declaration//GEN-END:variables

    @Override
    public void acceptVoiceCall() {
        this.isConnected = true;
        statusWhenCalling.setText("Connected");
        client.startVoiceChatThread();
    }

    @Override
    public void refuseVoiceCall() {
        this.dispose();
    }

}
