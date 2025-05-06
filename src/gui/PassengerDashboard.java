package gui;

import managers.BookingManager;
import managers.FlightManager;
import models.Booking;
import models.Flight;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PassengerDashboard extends JFrame {
    private JPanel mainContentPanel;
    private int passengerId;
    private String firstName;
    private JTextField searchField;
    private JPanel flightsContainer;

    public PassengerDashboard(int passengerId, String firstName) {
        this.passengerId = passengerId;
        this.firstName = firstName;

        setTitle("✈ Emirates Passenger Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel navBar = createNavBar();
        JPanel sidebar = createSidebar();
        mainContentPanel = new JPanel(new CardLayout());

        JPanel flightsPage = createFlightsPage();
        JPanel bookingsPage = createBookingsPage();

        mainContentPanel.add(flightsPage, "flights");
        mainContentPanel.add(bookingsPage, "myflights");

        add(navBar, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(240, 240, 240));
        navBar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel navTitle = new JLabel("✈️ Welcome, " + firstName);
        navTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(204, 0, 0));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setPreferredSize(new Dimension(100, 35));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        });

        navBar.add(navTitle, BorderLayout.WEST);
        navBar.add(logoutBtn, BorderLayout.EAST);

        return navBar;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(158, 27, 50));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Passenger Menu", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(title);

        String[] menuItems = {"flights", "myflights"};
        for (String item : menuItems) {
            String label = item.equals("flights") ? "Available Flights" : "My Flights";
            JButton btn = new JButton(label);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(160, 40));
            btn.setForeground(Color.BLACK);
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);

            btn.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContentPanel.getLayout();
                cl.show(mainContentPanel, item);
            });

            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(btn);
        }

        return sidebar;
    }

    private JPanel createFlightsPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("Available Flights", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.addActionListener(e -> loadFlights(searchField.getText().trim()));

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        flightsContainer = new JPanel();
        flightsContainer.setLayout(new BoxLayout(flightsContainer, BoxLayout.Y_AXIS));
        flightsContainer.setBackground(Color.WHITE);
        scrollPane.setViewportView(flightsContainer);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadFlights("");
        return panel;
    }

    private void loadFlights(String query) {
        flightsContainer.removeAll();
        List<Flight> flights = FlightManager.getInstance().getAllFlights();

        if (!query.isEmpty()) {
            flights = flights.stream()
                    .filter(f -> f.getFlightNumber().toLowerCase().contains(query.toLowerCase()) ||
                                 f.getOrigin().toLowerCase().contains(query.toLowerCase()) ||
                                 f.getDestination().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        for (Flight f : flights) {
            JPanel card = new JPanel(new BorderLayout());
            card.setPreferredSize(new Dimension(900, 130));
            card.setMaximumSize(new Dimension(900, 130));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    new EmptyBorder(15, 20, 15, 20)
            ));

            JPanel leftPanel = new JPanel(new GridLayout(2, 2));
            leftPanel.setOpaque(false);
            JLabel depTime = new JLabel("Departs: " + f.getDepartureTime());
            depTime.setFont(new Font("Segoe UI", Font.BOLD, 18));
            JLabel from = new JLabel("From: " + f.getOrigin());
            JLabel to = new JLabel("To: " + f.getDestination());
            JLabel seats = new JLabel("Seats Left: " + f.getAvailableSeats());
            for (JLabel label : new JLabel[]{depTime, from, to, seats}) {
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                leftPanel.add(label);
            }

            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            JLabel price = new JLabel("EUR 508.38", SwingConstants.RIGHT);
            price.setFont(new Font("Segoe UI", Font.BOLD, 20));
            price.setForeground(new Color(0, 102, 0));
            JButton bookBtn = new JButton("Book");
            bookBtn.setBackground(new Color(0, 102, 204));
            bookBtn.setForeground(Color.WHITE);
            bookBtn.setFocusPainted(false);
            bookBtn.setPreferredSize(new Dimension(100, 35));
            bookBtn.addActionListener(e -> {
                boolean booked = BookingManager.getInstance().bookFlight(passengerId, f.getId());
                if (booked) {
                    JOptionPane.showMessageDialog(this, "Flight booked successfully!");
                    reloadBookingsPage();
                } else {
                    JOptionPane.showMessageDialog(this, "Booking failed. No available seats.");
                }
            });

            rightPanel.add(price, BorderLayout.NORTH);
            rightPanel.add(bookBtn, BorderLayout.SOUTH);

            card.add(leftPanel, BorderLayout.CENTER);
            card.add(rightPanel, BorderLayout.EAST);
            flightsContainer.add(Box.createVerticalStrut(10));
            flightsContainer.add(card);
        }

        flightsContainer.revalidate();
        flightsContainer.repaint();
    }

    private void reloadBookingsPage() {
        mainContentPanel.remove(1);
        JPanel newBookingsPage = createBookingsPage();
        mainContentPanel.add(newBookingsPage, "myflights");
    }

    private JPanel createBookingsPage() {
        JPanel bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.setBackground(Color.WHITE);
        bookingsPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("My Bookings", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        bookingsPanel.add(title, BorderLayout.NORTH);

        String[] bookingCols = {"Booking ID", "Flight No", "From", "To", "Departure", "Date Booked"};
        DefaultTableModel bookingsModel = new DefaultTableModel(bookingCols, 0);
        JTable bookingsTable = new JTable(bookingsModel);
        JScrollPane scroll = new JScrollPane(bookingsTable);
        bookingsPanel.add(scroll, BorderLayout.CENTER);

        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> {
            int selected = bookingsTable.getSelectedRow();
            if (selected >= 0) {
                int bookingId = (int) bookingsModel.getValueAt(selected, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this booking?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = BookingManager.getInstance().cancelBooking(bookingId);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Booking canceled.");
                        mainContentPanel.remove(1);
                        mainContentPanel.add(createBookingsPage(), "myflights");
                        ((CardLayout) mainContentPanel.getLayout()).show(mainContentPanel, "myflights");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel booking.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(cancelButton);
        bookingsPanel.add(bottomPanel, BorderLayout.SOUTH);

        List<Booking> bookings = BookingManager.getInstance().getBookingsForUser(passengerId);
        for (Booking b : bookings) {
            Flight flight = FlightManager.getInstance().findFlightById(b.getFlightId());
            if (flight != null) {
                bookingsModel.addRow(new Object[]{
                    b.getId(),
                    flight.getFlightNumber(),
                    flight.getOrigin(),
                    flight.getDestination(),
                    flight.getDepartureTime(),
                    b.getBookingDate()
                });
            }
        }

        return bookingsPanel;
    }
}
