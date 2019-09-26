/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.controller.Client;
import client.controller.Command;
import client.model.Message;
import client.listener.MessageListener;
import client.listener.OnGetRoomsListener;
import client.listener.UserStatusListener;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import client.listener.RoomMemmberListener;
import client.model.RoomClientSide;

/**
 *
 * @author hoang
 */
public class MainChatClientScreen extends javax.swing.JFrame implements MessageListener, UserStatusListener, RoomMemmberListener {

    private Client client;
    private String roomName;
    private DefaultListModel listUserInRoom;
    private List<String> usersInRoom;

    public MainChatClientScreen(Client client, String roomName) {
        initComponents();
        this.client = client;
        this.roomName = roomName;
        jLabelRoomName.setText(roomName);
        listUserInRoom = new DefaultListModel();
        usersInRoom = new ArrayList<>();
        jList1.setModel(listUserInRoom);
        client.startMessageReader();
        jTextPane1.setEditable(false);
        client.setMessageListener(this);
        client.setUserStatusListener(this);
        client.sendMessage(Command.ROOM_MEMMBER, roomName, "");
        client.setRoomMemmberCallback(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jTextChat = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelRoomName = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
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

        jLabel2.setText("Room:");

        jLabelRoomName.setText("RoomName");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        jScrollPane3.setViewportView(jTextPane1);

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
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabelRoomName))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane3)
                        .addGap(18, 18, 18)))
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
            inputMessage("You: " + msg);
        }
    }//GEN-LAST:event_jTextChatKeyPressed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        client.sendMessage(Command.QUIT, roomName, "");
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        client.getRoomsClientSide();
        client.setOnGetRoomsListener((List<RoomClientSide> rooms) -> {
            System.out.println("**********get rooms coutn " + rooms.size());
            for(RoomClientSide room: rooms){
                System.out.println(room.getName());
            }
        });
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    public void showMessageReceive(Message message) throws BadLocationException {
        String msg = message.getUserName() + " : " + message.getBody();
        SimpleAttributeSet sas = new SimpleAttributeSet(jTextPane1.getCharacterAttributes());

        if (null != message.getCmd()) {
            switch (message.getCmd()) {
                //Checking if your output contains Exception...
                case LOGON:
                    StyleConstants.setForeground(sas, Color.red); //Changing the color of             
                    jTextPane1.getDocument().insertString(
                            jTextPane1.getDocument().getLength(),
                            msg + "has online \n",
                            sas
                    );
                    break;
                case LOGOFF:
                    StyleConstants.setForeground(sas, Color.DARK_GRAY); //Changing the color of             
                    jTextPane1.getDocument().insertString(
                            jTextPane1.getDocument().getLength(),
                            msg + "has offline \n",
                            sas
                    );
                    break;
                case SEND:
                    StyleConstants.setForeground(sas, Color.BLUE); //Changing the color of   
                    jTextPane1.getDocument().insertString(
                            jTextPane1.getDocument().getLength(),
                            msg,
                            sas
                    );
                    break;
                default:
                    break;
            }
        }

    }

    public void inputMessage(String msg) {
        SimpleAttributeSet sas = new SimpleAttributeSet(jTextPane1.getCharacterAttributes());
        StyleConstants.setForeground(sas, Color.BLACK); //Changing the color of             
        try {
            jTextPane1.getDocument().insertString(
                    jTextPane1.getDocument().getLength(),
                    msg + "\n",
                    sas
            );
        } catch (BadLocationException ble) {
            System.out.println("Error on append text to text input");
        }

    }

    @Override
    public void onMessageListener(Message message) {
        try {
            showMessageReceive(message);
        } catch (BadLocationException ex) {
            Logger.getLogger(MainChatClientScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onUserLogOn(Message msg) {
        String logOnUser = msg.getUserName();
        listUserInRoom.addElement(logOnUser);
        try {
            showMessageReceive(msg);
        } catch (BadLocationException ex) {
            Logger.getLogger(MainChatClientScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onUserLogOff(Message msg) {
        System.out.println("Geted offlien callback " + msg.getUserName());
        for (String user : usersInRoom) {
            if (user.equals(msg.getUserName())) {
                try {
                    usersInRoom.remove(user);
                    listUserInRoom.removeElement(user);
                    showMessageReceive(msg);
                    break;
                } catch (BadLocationException ex) {
                    Logger.getLogger(MainChatClientScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Override
    public void onRoomMemmberOnline(Message message) {
        String line = (String) message.getBody();
        System.out.println(line);
        String[] member = line.split(" ");
        System.out.println(member.length + " ");
        for (int i = 0; i < member.length - 1; i++) {
            System.out.println(member[i]);
            if (!member[i].equals(client.getUserName())) {
                listUserInRoom.addElement(member[i]);
                usersInRoom.add(member[i]);
            }
        }
    }
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextChat;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables

}
