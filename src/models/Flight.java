package models;

/**
 * ✅ Design Pattern: Data Transfer Object (DTO) / Plain Old Java Object (POJO)
 * ----------------------------------------------------------------------------
 * This class is used to encapsulate and transfer flight data across application layers.
 *
 
 */
public class Flight {
    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private String departureTime;
    private int availableSeats;

    public Flight(int id, String flightNumber, String origin, String destination, String departureTime, int availableSeats) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    // Getters
    public int getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public int getAvailableSeats() { return availableSeats; }

    // Setters
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
