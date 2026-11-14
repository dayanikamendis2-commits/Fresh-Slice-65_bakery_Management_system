package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{
    
    
    //ENCAPSULATION - private constants
    private static final String URL = "jdbc:mysql://localhost/bakery_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static Connection conn = null;
    
    private DBConnection() {}
    
     public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found.");
                throw new SQLException(e);
            }
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }
        
public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignore) {}
            conn = null;
        }
    }
}