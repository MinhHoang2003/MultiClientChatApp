/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.listener.OnInviteFriendListener;

/**
 *
 * @author hoang
 */
public class InviteFriendView extends javax.swing.JFrame {

    public InviteFriendView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        roomName = new javax.swing.JLabel();
        friendName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Berlin Sans FB", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Invite your friend to room : ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        roomName.setFont(new java.awt.Font("Berlin Sans FB", 0, 12)); // NOI18N
        roomName.setForeground(new java.awt.Color(255, 255, 255));
        roomName.setText("Room Name");
        getContentPane().add(roomName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        friendName.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        getContentPane().add(friendName, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 52, 234, 30));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Berlin Sans FB", 0, 12)); // NOI18N
        jButton1.setText("Invite");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.setBorderPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(197, 99, 70, 30));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Berlin Sans FB", 0, 12)); // NOI18N
        jButton2.setText("Cancel");
        jButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton2.setBorderPainted(false);
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(286, 99, 70, 30));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/room_backroudn_dark.jpg"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -6, 410, 150));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String friend = friendName.getText();
        if(!friend.isEmpty() && onStartInviteFriendListener!=null){
            onStartInviteFriendListener.onStart(friend);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField friendName;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel roomName;
    // End of variables declaration//GEN-END:variables
    private OnInviteFriendListener.OnStartInviteFriendListener onStartInviteFriendListener;

    public void setOnStartInviteFriendListener(OnInviteFriendListener.OnStartInviteFriendListener onStartInviteFriendListener) {
        this.onStartInviteFriendListener = onStartInviteFriendListener;
    }
    
    
}
