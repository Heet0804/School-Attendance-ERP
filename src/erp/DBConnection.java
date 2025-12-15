package erp;

import java.sql.*;

public class DBConnection {
    public static Connection connect() {
        Connection conn = null;
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to your database
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/attendance_erp", // replace 'mydb' with your database name
                "root", // your username
                ""      // your password (leave empty if none)
            );
            System.out.println("Connection Successful!");
        } catch(Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        connect();
    }
}
