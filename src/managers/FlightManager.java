package managers;

import db.DatabaseManager;
import models.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DESIGN PATTERNS USED:
 * ---------------------
 * ✅ Singleton Pattern
 *    - Ensures only one instance of FlightManager exists across the application.
 *    - BENEFIT: Centralizes flight-related logic and avoids inconsistent states.
 *
 * ✅ DAO (Data Access Object) Pattern
 *    - Provides an abstraction over the database operations for the "flights" table.
 *    - BENEFIT: Separation of data persistence logic from the UI/business layers.
 */
public class FlightManager {

    // === Singleton Instance ===
    private static FlightManager instance;

    // Private constructor to prevent instantiation
    private FlightManager() {}

    // Global access point for singleton instance
    public static FlightManager getInstance() {
        if (instance == null) {
            instance = new FlightManager();
        }
        return instance;
    }

    /**
     * Fetches all flights from the database.
     * DESIGN PATTERN: DAO
     */
    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("id"),
                        rs.getString("flight_number"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getInt("available_seats")
                );
                flights.add(flight);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }

    /**
     * Adds a new flight to the database.
     * DESIGN PATTERN: DAO
     */
    public void addFlight(Flight flight) {
        String sql = "INSERT INTO flights (flight_number, origin, destination, departure_time, available_seats) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, flight.getFlightNumber());
            pstmt.setString(2, flight.getOrigin());
            pstmt.setString(3, flight.getDestination());
            pstmt.setString(4, flight.getDepartureTime());
            pstmt.setInt(5, flight.getAvailableSeats());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a flight by ID from the database.
     * DESIGN PATTERN: DAO
     */
    public boolean removeFlight(int id) {
        String sql = "DELETE FROM flights WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds a flight by its ID.
     * DESIGN PATTERN: DAO
     */
    public Flight findFlightById(int id) {
        String sql = "SELECT * FROM flights WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Flight(
                        rs.getInt("id"),
                        rs.getString("flight_number"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getInt("available_seats")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
