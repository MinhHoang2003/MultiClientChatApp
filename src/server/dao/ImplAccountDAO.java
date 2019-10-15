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
import server.model.Account;

/**
 *
 * @author hoain
 */
public class ImplAccountDAO implements AccountDAO<Account> {

    @Override
    public boolean checkLogin(String username, String password) {
        boolean c = false;
        Connection con = null;
        CallableStatement callSt = null;
        if (username.equals("") || password.equals("")) {
            return c;
        }
        try {
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call getAccount(?,?)}");
            callSt.setString(1, username);
            callSt.setString(2, password);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                c = true;
            }
        } catch (Exception e) {
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
            callSt = con.prepareCall("{call getAccount(?,?)}");
            callSt.setString(1, username);
            callSt.setString(2, password);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                a.setUserName(rs.getString("UserName"));
                a.setPassword("");
            }
        } catch (Exception e) {
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
            con = ConnectionDB.openConnection();
            callSt = con.prepareCall("{call insertAccount(?,?)}");
            callSt.setString(1, username);
            callSt.setString(2, password);
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
        } catch (Exception e) {
            System.out.println(e);
            c = false;
        } finally {
            ConnectionDB.closeConnection(con, callSt);
        }
        return c;
    }

}
