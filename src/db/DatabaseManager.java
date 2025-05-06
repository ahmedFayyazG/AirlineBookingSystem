package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Design Pattern Used: Simple Factory 
 * ---------------------------------------------------
 * This class encapsulates the creation of database Connection objects.
 * It hides the logic of how a Connection is created (Factory Method Pattern),
 * and can be extended into a Singleton for shared access and better control.
 */
public class DatabaseManager {
    // Database connection settings
    private static final String URL = "jdbc:postgresql://localhost:3000/airline_db";
    private static final String USER = "postgres";           // your PostgreSQL username
    private static final String PASSWORD = "Islamabad@000";  // your PostgreSQL password

    /**
     * Factory Method
     * ----------------------------
     * This method creates and returns a new database connection object.
     * It centralizes connection creation logic and can be enhanced with logging,
     * connection pooling, or error tracking later.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
