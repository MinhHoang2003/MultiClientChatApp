/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author hoain
 */
public class ConnectionDB {

    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ChatApp";
    private static final String USER = "sa";
    private static final String PASSWORD = "nam";

    public static Connection openConnection() {
        Connection con = null;
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }

    public static void closeConnection(Connection con, CallableStatement callSt) {
        try {
            if (con != null) {
                con.close();
            }
            if (callSt != null) {
                callSt.close();
            }
        } catch (Exception e) {
        }
    }
}
