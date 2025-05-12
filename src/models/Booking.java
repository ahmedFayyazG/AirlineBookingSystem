package models;

/**
 * ✅ Design Pattern: Data Transfer Object (DTO) / Plain Old Java Object (POJO)
 * ----------------------------------------------------------------------------
 * This class represents a simple data container for transferring booking-related data.
 *
 * 🔍 Why DTO/POJO?
 * - It encapsulates booking data without business logic.
 * - Ensures clean data transfer between layers (GUI ↔ Manager ↔ Database).
 * - Promotes **separation of concerns** by keeping data and logic separate.
 */
public class Booking {
    private int id;
    private int userId;
    private int flightId;
    private String bookingDate;

    public Booking(int id, int userId, int flightId, String bookingDate) {
        this.id = id;
        this.userId = userId;
        this.flightId = flightId;
        this.bookingDate = bookingDate;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getFlightId() { return flightId; }
    public String getBookingDate() { return bookingDate; }
}
