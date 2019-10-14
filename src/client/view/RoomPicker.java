/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.controller.Client;
import client.listener.OnGetRoomsListener;
import client.listener.OnJoinRoomListener;
import client.listener.OnShowRoomPickerListener;
import client.model.RoomClientSide;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import server.model.RoomStatus;

/**
 *
 * @author hoang
 */
public class RoomPicker extends javax.swing.JFrame implements
        OnGetRoomsListener, OnJoinRoomListener, OnShowRoomPickerListener {

    /**
     * Creates new form RoomPicker
     */
    private DefaultListModel<RoomClientSide> model;
    private Client client;
    private List<MainChatClientScreen> chatViews;
    private RoomClientSide rcs;

    public RoomPicker(Client client) {
        initComponents();
        this.client = client;
        chatViews = new ArrayList<>();
        client.startMessageReader();
        client.getRoomsClientSide();
        client.addOnGetRoomsListener(this);
        client.addOnJoinRoomListener(this);
    }

    private void setUp(List<RoomClientSide> rooms) {
        // create List model
        model = new DefaultListModel<>();
        // add item to model
        for (RoomClientSide room : rooms) {
            model.addElement(room);
        }
        // create JList with model       
        jListRooms.setModel(model);
        jListRooms.setCellRenderer(new RoomRenderer());
        jListRooms.setBackground(Color.WHITE);;
        jListRooms.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    rcs = jListRooms.getSelectedValue();
                    if (rcs == null) {
                        return;
                    }
                    if (rcs.getRoomStatus() == RoomStatus.PRIVATE) {
                        showInputPasswordBox();
                    } else {
                        try {
                            client.joinRoom(rcs.getName(), null);
                        } catch (IOException ex) {
                            Logger.getLogger(RoomPicker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

    private void showInputPasswordBox() {
        JPasswordField pwd = new JPasswordField(10);
        int action = JOptionPane.showConfirmDialog(null, pwd, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
        System.out.println(action + "");
        if (action != 0) {
            JOptionPane.showMessageDialog(null, "Cancel, X or escape key selected");
        } else {
            try {
                client.joinRoom(rcs.getName(), new String(pwd.getPassword()));
            } catch (IOException ex) {
                Logger.getLogger(RoomPicker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListRooms = new javax.swing.JList<RoomClientSide>();
        jLabel1 = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Choise your room!!!");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        jListRooms.setForeground(new java.awt.Color(255, 255, 255));
        jListRooms.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(jListRooms);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 310, 430));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/room_backroudn_dark.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 310, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<RoomClientSide> jListRooms;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onGetRooms(List<RoomClientSide> rooms) {
        System.out.println(rooms.size() + "");
        setUp(rooms);
    }

    @Override
    public void onJoinRoomSuccessful(String msg) {
        MainChatClientScreen newRoom = new MainChatClientScreen(client, msg);
        newRoom.setOnShowRoomPickerListener(this);
        chatViews.add(newRoom);
        newRoom.setTitle(client.getUserName());
        newRoom.setVisible(true);
        this.setVisible(false);

        newRoom.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                chatViews.remove(newRoom);
                System.out.println("ChatViews size: " + chatViews.size());
                System.out.println("Frame: " + newRoom.getTitle());
                if (chatViews.isEmpty()) {
                    RoomPicker.this.setVisible(true);
                    RoomPicker.this.jListRooms.clearSelection();
                }
            }
        });
    }

    @Override
    public void onJoinRoomFail(String msg) {
        JOptionPane.showMessageDialog(this, "Wrong password!!!");
        jListRooms.clearSelection();
    }

    @Override
    public void onShowRoomPikcer() {
        RoomPicker.this.setVisible(true);
        RoomPicker.this.jListRooms.clearSelection();
    }
    
}
