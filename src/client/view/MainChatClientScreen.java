/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.Client;
import client.Command;
import client.Message;
import client.MessageListener;
import client.UserStatusListener;
import java.awt.event.KeyEvent;
import javax.swing.DefaultListModel;

/**
 *
 * @author hoang
 */
public class MainChatClientScreen extends javax.swing.JFrame implements MessageListener,UserStatusListener{

    /**
     * Creates new form MainChatClientScreen
     */
    public MainChatClientScreen(Client client, String roomName) {
        initComponents();
        this.client = client;
        this.roomName = roomName;
        jLabelRoomName.setText(roomName);
        listUserInRoom = new DefaultListModel();
        jList1.setModel(listUserInRoom);
        client.startMessageReader();
        client.setMessageListener(this);
        client.setUserStatusListener(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jTextChat = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextChatArea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabelRoomName = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextChatActionPerformed(evt);
            }
        });
        jTextChat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextChatKeyPressed(evt);
            }
        });

        jLabel1.setText("Chat here:");

        jTextChatArea.setColumns(20);
        jTextChatArea.setRows(5);
        jScrollPane1.setViewportView(jTextChatArea);

        jLabel2.setText("Room:");

        jLabelRoomName.setText("RoomName");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        jMenu2.setText("Account");

        jMenuItem2.setText("Quit/Logoff");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Room");

        jMenuItem3.setText("Join Room");
        jMenu3.add(jMenuItem3);

        jMenuItem4.setText("Create Room");
        jMenu3.add(jMenuItem4);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelRoomName))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextChat, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(132, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabelRoomName))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 84, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextChatActionPerformed

    }//GEN-LAST:event_jTextChatActionPerformed

    private void jTextChatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextChatKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String msg = jTextChat.getText();
            client.sendMessage(Command.SEND, "General", msg);
            jTextChat.setText("");
            jTextChatArea.append("You: " + msg + "\n");
        }
    }//GEN-LAST:event_jTextChatKeyPressed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        client.sendMessage(Command.QUIT, "System", "");
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    public void showMessageReceive(Message message) {
        String msg = message.getUserName() + " : " + message.getBody()+"\n";
        jTextChatArea.append(msg);
    }
    private Client client;
    private String roomName;
    private DefaultListModel listUserInRoom;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelRoomName;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextChat;
    private javax.swing.JTextArea jTextChatArea;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onMessageListener(Message message) {
        showMessageReceive(message);
    }

    @Override
    public void onUserLogOn(Message msg) {
        String logOnUser = msg.getUserName();
        listUserInRoom.addElement(logOnUser);
    }

    @Override
    public void onUserLogOff(Message msg) {
        
    }
}
