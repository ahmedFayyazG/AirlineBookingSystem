package gui;

import managers.FlightManager; // Singleton Pattern Used
import models.Flight;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Design Patterns Used:
 * ---------------------
 * ✅ Singleton Pattern (via FlightManager)
 *    - Ensures centralized and consistent access to all flight data.
 *    - Simplifies state management throughout the app.
 *     *    - Prevents multiple instances 
 * ✅ MVC Pattern (View Component)
 *    - FlightsPage represents the 'View' in the MVC architecture.
 *    - It fetches data from the model (FlightManager/Flight) and updates the UI accordingly.
 */
public class FlightsPage extends JPanel {
    private JPanel cardContainer;
    private DefaultTableModel tableModel;

    public FlightsPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("All Flights", JLabel.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(150, 0, 0));
        title.setBorder(new EmptyBorder(20, 40, 10, 0));

        JButton addButton = new JButton("+ Add Flight");
        addButton.setBackground(new Color(0, 102, 204));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addButton.setPreferredSize(new Dimension(140, 35));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(addButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // === Flight Table ===
        String[] columns = {"ID", "Flight No", "Origin", "Destination", "Time", "Seats"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 120));
        table.setFillsViewportHeight(true);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(new EmptyBorder(10, 40, 10, 40));
        contentPanel.add(tableScroll, BorderLayout.PAGE_START);

        // === Flight Cards ===
        cardContainer = new JPanel(new GridLayout(0, 2, 20, 20));
        cardContainer.setBorder(new EmptyBorder(20, 40, 20, 40));
        cardContainer.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(cardContainer);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scroll, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        addButton.addActionListener(e -> showAddFlightDialog());

        refreshFlightData();
    }

    /**
     * MVC Controller Logic:
     * ---------------------
     * Refreshes the data from the model (FlightManager),
     * updates both the table and the cards in the UI (View).
     */
    private void refreshFlightData() {
        tableModel.setRowCount(0);
        cardContainer.removeAll();

        List<Flight> flights = FlightManager.getInstance().getAllFlights(); // Singleton usage
        for (Flight f : flights) {
            tableModel.addRow(new Object[] {
                f.getId(), f.getFlightNumber(), f.getOrigin(),
                f.getDestination(), f.getDepartureTime(), f.getAvailableSeats()
            });

            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(new Color(250, 250, 250));
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(15, 15, 15, 15)
            ));

            JPanel textPanel = new JPanel(new GridLayout(6, 1));
            textPanel.setOpaque(false);
            JLabel id = new JLabel("Flight ID: " + f.getId());
            JLabel number = new JLabel("Flight No: " + f.getFlightNumber());
            JLabel origin = new JLabel("From: " + f.getOrigin());
            JLabel destination = new JLabel("To: " + f.getDestination());
            JLabel time = new JLabel("Time: " + f.getDepartureTime());
            JLabel seats = new JLabel("Seats: " + f.getAvailableSeats());

            for (JLabel label : new JLabel[]{id, number, origin, destination, time, seats}) {
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                textPanel.add(label);
            }

            JButton deleteBtn = new JButton("Delete");
            deleteBtn.setBackground(new Color(204, 0, 0));
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setPreferredSize(new Dimension(100, 30));
            deleteBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(null, "Delete this flight?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    FlightManager.getInstance().removeFlight(f.getId());
                    refreshFlightData(); // Reflect changes immediately
                }
            });

            card.add(textPanel, BorderLayout.CENTER);
            card.add(deleteBtn, BorderLayout.SOUTH);
            cardContainer.add(card);
        }

        cardContainer.revalidate();
        cardContainer.repaint();
    }

    /**
     * MVC Controller Logic:
     * ---------------------
     * Handles user input via dialog, interacts with the model to save new data.
     */
    private void showAddFlightDialog() {
        JTextField flightNo = new JTextField();
        JTextField origin = new JTextField();
        JTextField destination = new JTextField();
        JTextField time = new JTextField();
        JTextField seats = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Flight No:")); panel.add(flightNo);
        panel.add(new JLabel("Origin:")); panel.add(origin);
        panel.add(new JLabel("Destination:")); panel.add(destination);
        panel.add(new JLabel("Departure Time:")); panel.add(time);
        panel.add(new JLabel("Seats:")); panel.add(seats);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Flight", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = FlightManager.getInstance().getAllFlights().size() + 1;
                Flight flight = new Flight(id, flightNo.getText(), origin.getText(), destination.getText(), time.getText(), Integer.parseInt(seats.getText()));
                FlightManager.getInstance().addFlight(flight);
                refreshFlightData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for seats.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
