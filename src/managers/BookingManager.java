package managers;

import db.DatabaseManager;
import models.Booking;
import models.Flight;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DESIGN PATTERNS USED:
 * ---------------------
 * ✅ Singleton Pattern
 *    - Ensures only one instance of BookingManager handles all booking-related operations.
 *    - BENEFIT: Prevents state conflicts, centralizes booking logic for consistency.
 *
 * ✅ Data Access Object (DAO) Pattern
 *    - This class abstracts the database operations (insert, select, update, delete) for bookings.
 *    - BENEFIT: Clean separation of persistence logic from business logic.
 */
public class BookingManager {

    // === Singleton Pattern ===
    private static BookingManager instance = null;

    // Private constructor prevents direct instantiation
    private BookingManager() {}

    // Global access point to the singleton instance
    public static BookingManager getInstance() {
        if (instance == null) {
            instance = new BookingManager();
        }
        return instance;
    }

    /**
     * Cancels a booking and updates the available seats in the flight table.
     * DESIGN PATTERN: DAO (Database Access)
     * BENEFIT: All database logic related to bookings is encapsulated in this method.
     */
    public boolean cancelBooking(int bookingId) {
        String getFlightIdSql = "SELECT flight_id FROM bookings WHERE id = ?";
        String deleteSql = "DELETE FROM bookings WHERE id = ?";
        String updateSeatsSql = "UPDATE flights SET available_seats = available_seats + 1 WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);  // Manual transaction control

            int flightId = -1;

            // Retrieve associated flight ID
            try (PreparedStatement getStmt = conn.prepareStatement(getFlightIdSql)) {
                getStmt.setInt(1, bookingId);
                ResultSet rs = getStmt.executeQuery();
                if (rs.next()) {
                    flightId = rs.getInt("flight_id");
                } else {
                    conn.rollback();
                    return false;
                }
            }

            // Delete booking
            try (PreparedStatement delStmt = conn.prepareStatement(deleteSql)) {
                delStmt.setInt(1, bookingId);
                delStmt.executeUpdate();
            }

            // Update available seats
            try (PreparedStatement seatStmt = conn.prepareStatement(updateSeatsSql)) {
                seatStmt.setInt(1, flightId);
                seatStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Books a flight for a user and decreases seat availability.
     * DESIGN PATTERN: DAO + Transaction Management
     * BENEFIT: Encapsulates multiple related operations (update + insert) within a safe transaction.
     */
    public boolean bookFlight(int userId, int flightId) {
        String bookingSql = "INSERT INTO bookings (user_id, flight_id, booking_date) VALUES (?, ?, ?)";
        String updateSeatsSql = "UPDATE flights SET available_seats = available_seats - 1 WHERE id = ? AND available_seats > 0";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false); // Begin transaction

            // Update available seats
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSeatsSql)) {
                updateStmt.setInt(1, flightId);
                int affected = updateStmt.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    System.out.println("❌ Booking failed. No available seats.");
                    return false;
                }
            }

            // Insert booking record
            try (PreparedStatement bookStmt = conn.prepareStatement(bookingSql)) {
                bookStmt.setInt(1, userId);
                bookStmt.setInt(2, flightId);
                bookStmt.setDate(3, Date.valueOf(LocalDate.now()));
                bookStmt.executeUpdate();
            }

            conn.commit(); // All good
            System.out.println("✅ Booking successful!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all bookings for a given user.
     * DESIGN PATTERN: DAO
     * BENEFIT: Abstracts and centralizes booking retrieval logic from the database.
     */
    public List<Booking> getBookingsForUser(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("flight_id"),
                        rs.getDate("booking_date").toString()
                );
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}
