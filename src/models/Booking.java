package models;


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
