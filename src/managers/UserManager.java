package managers;

import db.DatabaseManager;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ✅ Design Pattern: Singleton
 * ----------------------------
 * This class uses the Singleton Pattern to ensure only one instance of UserManager exists.
 * 
 * 🔍 Why Singleton?
 * - Centralized control of user operations (registration, login, etc.).
 * - Efficient reuse of shared logic without creating multiple instances.
 */
public class UserManager {
    private static UserManager instance;

    // Private constructor to prevent instantiation
    private UserManager() {}

    // Global access point to the single instance
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * Registers a new user in the database.
     */
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, phone, username, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getUsername());
            stmt.setString(6, user.getPassword());
            stmt.setString(7, user.getRole());
    
            int rows = stmt.executeUpdate();
            return rows > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Allows the admin or system to programmatically create users.
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, phone, username, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getUsername());
            stmt.setString(6, user.getPassword());
            stmt.setString(7, user.getRole());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates user credentials for login.
     */
    public User validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a list of all users in the system.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
