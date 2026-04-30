package com.namastemart.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBUtil {

    private DBUtil() {
        // Prevent object creation
    }

    // ================= GET CONNECTION =================
    public static Connection getConnection() {

        Connection conn = null;

        try {
            ResourceBundle rb = ResourceBundle.getBundle("application");

            String connectionString = rb.getString("db.connectionString");
            String driverName = rb.getString("db.driverName");
            String username = rb.getString("db.username");
            String password = rb.getString("db.password");

            Class.forName(driverName);

            conn = DriverManager.getConnection(connectionString, username, password);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    // ================= ALIAS - provideConnection() =================
    public static Connection provideConnection() {
        return getConnection();
    }

    // ================= CLOSE CONNECTION =================
    public static void closeConnection(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(PreparedStatement ps) {
        try {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}