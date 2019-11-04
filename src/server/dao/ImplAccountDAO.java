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
import java.sql.SQLException;
import server.model.Account;

import org.mindrot.jbcrypt.BCrypt; // Lib hash password

/**
 *
 * @author hoain
 */
public class ImplAccountDAO implements AccountDAO {

    @Override
    public boolean checkLogin(String username, String password) { // using login
        boolean c = false;
        Connection con = null;
        CallableStatement callSt = null;
        if (username.equals("") || password.equals("")) {
            return c;
        }
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call getAccount(?)}");
            callSt.setString(1, username);
//            callSt.setString(2, password);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                String hash = rs.getString("Password");
                boolean valuate = BCrypt.checkpw(password, hash);
                if (valuate) {
                    c = true;
                } else {
                    c = false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            c = false;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return c;
    }

    @Override
    public Account getAccount(String username, String password) {
        Account a = new Account();
        Connection con = null;
        CallableStatement callSt = null;
        if (username.equals("") || password.equals("")) {
            return null;
        }
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call getAccount(?)}");
            callSt.setString(1, username);
//            callSt.setString(2, password);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                a.setUserName(rs.getString("UserName"));
                a.setPassword("");
            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return a;
    }

    @Override
    public boolean registerAccount(String username, String password) {
        boolean c = true;
        Connection con = null;
        CallableStatement callSt = null;
        if (username.equals("") || password.equals("")) {
            return false;
        }
        try {
            // hash pass
            String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));

            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call insertAccount(?,?)}");
            callSt.setString(1, username);
            callSt.setString(2, hash);
            callSt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            c = false;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return c;
    }

    @Override
    public boolean checkExistAccount(String username) {
        boolean c = false;
        Connection con = null;
        CallableStatement callSt = null;
        if (username.equals("")) {
            return c;
        }
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call checkExitAccount(?)}");
            callSt.setString(1, username);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                c = true;
            }
        } catch (SQLException e) {
            c = false;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return c;
    }

}
