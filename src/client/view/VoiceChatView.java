/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.controller.Client;

/**
 *
 * @author hoang
 */
public class VoiceChatView extends javax.swing.JFrame {
    
    public static int MAKE_A_CALL = 1;
    public static int RECEIVE_A_CALL = 2;

    private final Client client;
    private final String roomName;
    private final int callState;

    public VoiceChatView(String roomName, Client client,int callState) {
        initComponents();
        this.roomName = roomName;
        this.room.setText(roomName);
        this.client = client;
        this.callState = callState;
        if(callState == MAKE_A_CALL){
            client.makeVoiceCall(roomName);
        }else {
            onCalling.setEnabled(false);
        }
        room.setText(roomName);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        stop = new javax.swing.JLabel();
        onCalling = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        room = new javax.swing.JLabel();

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

        onCalling.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/onCalling.png"))); // NOI18N
        onCalling.setText("On Calling");
        onCalling.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onCallingMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel4.setText("Voice call on: ");

        room.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        room.setText("jLabel5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(stop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(onCalling)
                .addGap(35, 35, 35))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(room)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(onCalling)
                    .addComponent(stop))
                .addGap(57, 57, 57))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopMouseClicked
        client.stopVoiceChatThread();
        this.dispose();
    }//GEN-LAST:event_stopMouseClicked

    private void onCallingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onCallingMouseClicked
        client.startVoiceChatThread();
    }//GEN-LAST:event_onCallingMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel onCalling;
    private javax.swing.JLabel room;
    private javax.swing.JLabel stop;
    // End of variables declaration//GEN-END:variables

}
