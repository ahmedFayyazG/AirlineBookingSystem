package managers;

import db.DatabaseManager;
import models.Booking;
import models.Flight;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingManager {
    private static BookingManager instance = null;

    private BookingManager() {}

    public static BookingManager getInstance() {
        if (instance == null) {
            instance = new BookingManager();
        }
        return instance;
    }

    public boolean cancelBooking(int bookingId) {
        String getFlightIdSql = "SELECT flight_id FROM bookings WHERE id = ?";
        String deleteSql = "DELETE FROM bookings WHERE id = ?";
        String updateSeatsSql = "UPDATE flights SET available_seats = available_seats + 1 WHERE id = ?";
    
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
    
            int flightId = -1;
    
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
    
            try (PreparedStatement delStmt = conn.prepareStatement(deleteSql)) {
                delStmt.setInt(1, bookingId);
                delStmt.executeUpdate();
            }
    
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
    


    public boolean bookFlight(int userId, int flightId) {
        String bookingSql = "INSERT INTO bookings (user_id, flight_id, booking_date) VALUES (?, ?, ?)";
        String updateSeatsSql = "UPDATE flights SET available_seats = available_seats - 1 WHERE id = ? AND available_seats > 0";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            // Decrease seat count
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSeatsSql)) {
                updateStmt.setInt(1, flightId);
                int affected = updateStmt.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    System.out.println("❌ Booking failed. No available seats.");
                    return false;
                }
            }

            // Add booking record
            try (PreparedStatement bookStmt = conn.prepareStatement(bookingSql)) {
                bookStmt.setInt(1, userId);
                bookStmt.setInt(2, flightId);
                bookStmt.setDate(3, Date.valueOf(LocalDate.now()));
                bookStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Booking successful!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
