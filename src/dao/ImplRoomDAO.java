/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import client.model.RoomClientSide;
import connectDB.ConnectionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import server.model.MessInRoom;
import server.model.RoomStatus;

/**
 *
 * @author hoain
 */
public class ImplRoomDAO implements RoomDAO {

    @Override
    public boolean insertMess(String roomName, String username, String content) {
        boolean c = true;
        Connection con = null;
        CallableStatement callSt = null;
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call insertMess(?,?,?)}");
            callSt.setString(1, roomName);
            callSt.setString(2, username);
            callSt.setString(3, content);
            callSt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            c = false;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return c;
    }

    @Override
    public List<MessInRoom> getMessByRoomName(String roomName) {
        /**
         * Truyền vào tên phòng và nhận lại các tin nhắn trong phòng săp xếp
         * theo thứ tự từ cũ nhất đên mới nhất
         */
        Connection con = null;
        CallableStatement callSt = null;
        List<MessInRoom> listMess = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call getMesByRoomName(?)}");
            callSt.setString(1, roomName);
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                MessInRoom m = new MessInRoom();
                m.setRoomName(rs.getString("RoomName"));
                m.setUsername(rs.getString("UserName"));
                m.setContent(rs.getString("Content"));
                listMess.add(m);
            }
        } catch (Exception e) {
            return null;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return listMess;
    }

    @Override
    public List<RoomClientSide> getRoomClient() {
        Connection con = null;
        CallableStatement callSt = null;
        List<RoomClientSide> listRoom = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call getRoomClient()}");
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                RoomClientSide r = new RoomClientSide();
                r.setName(rs.getString("RoomName"));
                if (rs.getInt("Status") == 1) {
                    r.setRoomStatus(RoomStatus.PUBLIC);
                } else {
                    r.setRoomStatus(RoomStatus.PRIVATE);
                }
                listRoom.add(r);
            }
        } catch (Exception e) {
            return null;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return listRoom;
    }

    @Override
    public boolean checkExitsRoom(String roomName) {
        Connection con = null;
        CallableStatement callSt = null;
        boolean check = false;
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call getRoomByName(?)}");
            callSt.setString(1, roomName);
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                check = true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return check;
    }

    @Override
    public boolean checkOwner(String roomName, String owner) {
        Connection con = null;
        CallableStatement callSt = null;
        boolean check = false;
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call getOwnerByRoomName(?)}");
            callSt.setString(1, roomName);
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                String s = rs.getString("Owner");
                if (s.equals(owner)) {
                    check = true;
                } else {
                    check = false;
                }
            }
        } catch (Exception e) {
            return false;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return check;
    }

    @Override
    public boolean deleteRoomByName(String roomName) {
        boolean c = true;
        Connection con = null;
        CallableStatement callSt = null;
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call deleteRoomByName(?)}");
            callSt.setString(1, roomName);
            callSt.executeUpdate();
        } catch (Exception e) {
//            System.out.println(e);
            c = false;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return c;
    }

    @Override
    public boolean addRoom(String roomName, String password, int status, String owner) {
        boolean c = true;
        Connection con = null;
        CallableStatement callSt = null;
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call addRoom(?,?,?,?)}");
            callSt.setString(1, roomName);
            callSt.setString(2, password);
            callSt.setInt(3, status);
            callSt.setString(4, owner);
            callSt.executeUpdate();
        } catch (Exception e) {
//            System.out.println(e);
            c = false;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return c;
    }

    @Override
    public RoomClientSide getRoomByName(String roomName) {
        Connection con = null;
        CallableStatement callSt = null;
        RoomClientSide room = null;
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call getRoomByName(?)}");
            callSt.setString(1, roomName);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("Status") == 1) {
                    room = new RoomClientSide(rs.getString("RoomName"), RoomStatus.PUBLIC);
                } else {
                    room = new RoomClientSide(rs.getString("RoomName"), RoomStatus.PRIVATE);
                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return room;
    }

    @Override
    public boolean checkPassword(String roomName, String password) {
        boolean c = false;
        Connection con = null;
        CallableStatement callSt = null;
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call checkPassword(?,?)}");
            callSt.setString(1, roomName);
            callSt.setString(2, password);          
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                c = true;
            }
        } catch (Exception e) {
//            System.out.println(e);
            c = false;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return c;
    }

}
