package test.managers;

import managers.FlightManager;
import models.Flight;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class FlightManagerTest {

    @Test
    public void testSingletonInstance() {
        FlightManager m1 = FlightManager.getInstance();
        FlightManager m2 = FlightManager.getInstance();
        assertSame(m1, m2);
    }

    @Test
    public void testAddAndGetFlight() {
        FlightManager manager = FlightManager.getInstance();

        Flight newFlight = new Flight(0, "JUNIT101", "TestCityA", "TestCityB", "12:00", 10);
        manager.addFlight(newFlight);

        List<Flight> flights = manager.getAllFlights();
        boolean found = flights.stream().anyMatch(f ->
            f.getFlightNumber().equals("JUNIT101") &&
            f.getOrigin().equals("TestCityA") &&
            f.getDestination().equals("TestCityB")
        );

        assertTrue("New flight should exist in the list", found);
    }

    @Test
    public void testRemoveFlight() {
        FlightManager manager = FlightManager.getInstance();

        Flight tempFlight = new Flight(0, "REMOVE123", "CityX", "CityY", "15:00", 5);
        manager.addFlight(tempFlight);

        Flight added = manager.getAllFlights().stream()
            .filter(f -> f.getFlightNumber().equals("REMOVE123"))
            .findFirst()
            .orElse(null);

        assertNotNull("Flight should be added before removing", added);

        boolean removed = manager.removeFlight(added.getId());
        assertTrue("Flight should be successfully removed", removed);
    }
}



//java -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/postgresql-42.7.5.jar;out;." org.junit.runner.JUnitCore test.managers.FlightManagerTest
