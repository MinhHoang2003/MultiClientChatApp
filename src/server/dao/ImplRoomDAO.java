/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.dao;

import connectDB.ConnectionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import server.model.MessInRoom;

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
         * Truyền vào tên phòng và 
         * nhận lại các tin nhắn trong 
         * phòng săp xếp theo thứ tự từ 
         * cũ nhất đên mới nhất
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

}
