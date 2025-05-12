package test.managers;

import managers.BookingManager;
import managers.FlightManager;
import models.Booking;
import models.Flight;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BookingManagerTest {

    @Test
    public void testBookFlight() {
        FlightManager fm = FlightManager.getInstance();
        BookingManager bm = BookingManager.getInstance();

        Flight testFlight = new Flight(0, "BM123", "Alpha", "Beta", "09:00", 5);
        fm.addFlight(testFlight);

        // Get the ID of the added flight
        int flightId = fm.getAllFlights().stream()
                .filter(f -> f.getFlightNumber().equals("BM123"))
                .findFirst().get().getId();

        boolean booked = bm.bookFlight(1, flightId); // assuming user ID 1 exists
        assertTrue("Booking should succeed", booked);
    }

    @Test
    public void testGetBookingsForUser() {
        BookingManager bm = BookingManager.getInstance();
        List<Booking> bookings = bm.getBookingsForUser(1); // assuming user ID 1 has bookings
        assertNotNull("Bookings list should not be null", bookings);
    }

    @Test
    public void testCancelBooking() {
        BookingManager bm = BookingManager.getInstance();
        List<Booking> bookings = bm.getBookingsForUser(1);

        if (!bookings.isEmpty()) {
            int bookingId = bookings.get(0).getId();
            boolean canceled = bm.cancelBooking(bookingId);
            assertTrue("Booking should be canceled", canceled);
        } else {
            System.out.println("No bookings found to test cancelBooking(). Skipped.");
        }
    }
}


//java -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/postgresql-42.7.5.jar;out;." org.junit.runner.JUnitCore test.managers.BookingManagerTest
//javac -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/postgresql-42.7.5.jar;out;." -d out test/managers/BookingManagerTest.java
