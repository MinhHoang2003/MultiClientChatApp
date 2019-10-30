/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.listener.OnCreateRoomListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JOptionPane;

/**
 *
 * @author hoang
 */
public class CreateRoomView extends javax.swing.JFrame {

    public CreateRoomView() {
        initComponents();
        showPassowrdField();
        roomType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                showPassowrdField();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        roomName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        roomType = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        roomPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Room Name");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 65, -1, -1));
        getContentPane().add(roomName, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 62, 186, -1));

        jLabel2.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Room Type");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 103, -1, -1));

        roomType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Public", "Private" }));
        getContentPane().add(roomType, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 100, 91, -1));

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Password");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 138, -1, -1));
        getContentPane().add(roomPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 138, 186, -1));

        jLabel4.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Fill this form to creat new room");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 20, -1, -1));

        btnCreate.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        btnCreate.setText("Create");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });
        getContentPane().add(btnCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(246, 191, -1, -1));

        btnCancel.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        btnCancel.setText("Cancel");
        getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 191, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/room_backroudn_dark.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, -1, 420, 230));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        String roomName = this.roomName.getText();
        System.out.println(roomName);
        String roomType = (String) this.roomType.getSelectedItem();
        String pass = new String(roomPassword.getPassword());
        if (roomName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room Name must not empty");
            return;
        }
        if (roomType.equalsIgnoreCase("Private") && pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password must not empty");
            return;
        }
        this.onStartCreateRoom.onStart(roomName, roomType, pass);
    }//GEN-LAST:event_btnCreateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCreate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField roomName;
    private javax.swing.JPasswordField roomPassword;
    private javax.swing.JComboBox<String> roomType;
    // End of variables declaration//GEN-END:variables

    private OnCreateRoomListener.OnStartCreateRoom onStartCreateRoom;

    public void setOnStartCreateRoom(OnCreateRoomListener.OnStartCreateRoom onStartCreateRoom) {
        this.onStartCreateRoom = onStartCreateRoom;
    }

    private void showPassowrdField() {
        if (roomType.getSelectedItem().toString().equals("Public")) {
            roomPassword.setEnabled(false);
        } else {
            roomPassword.setEnabled(true);
        }
    }

}
