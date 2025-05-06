package gui;

import managers.FlightManager;
import managers.UserManager;
import models.Flight;
import models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public AdminDashboard() {
        setTitle("✈ Emirates Admin Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        JPanel topNav = createTopNavbar();
        mainContentPanel = createMainContent();

        add(topNav, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createTopNavbar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(245, 245, 245));
        navBar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(204, 0, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        navBar.add(title, BorderLayout.WEST);
        navBar.add(logoutButton, BorderLayout.EAST);

        return navBar;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(158, 27, 50));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("FLY EMIRATES", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(new EmptyBorder(30, 0, 20, 0));
        sidebar.add(logo);

        String[] items = {"Dashboard", "Flights", "Users", "Settings"};
        for (String label : items) {
            JButton button = new JButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(180, 40));
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                cardLayout.show(mainContentPanel, label.toLowerCase());
            });

            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(button);
        }

        return sidebar;
    }

    private JPanel createMainContent() {
        cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        container.add(createDashboardPanel(), "dashboard");
        container.add(new FlightsPage(), "flights");
        container.add(wrapInScrollPane(createUsersPanel()), "users");
        container.add(createSettingsPanel(), "settings");

        return container;
    }

    private JPanel createDashboardPanel() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);

        JPanel kpiPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        kpiPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        kpiPanel.setBackground(Color.WHITE);

        List<Flight> flights = FlightManager.getInstance().getAllFlights();
        List<User> users = UserManager.getInstance().getAllUsers();

        int totalFlights = flights.size();
        int totalSeats = flights.stream().mapToInt(Flight::getAvailableSeats).sum();
        long totalPassengers = users.stream().filter(u -> u.getRole().equalsIgnoreCase("passenger")).count();

        kpiPanel.add(createKpiCard("Total Flights", String.valueOf(totalFlights)));
        kpiPanel.add(createKpiCard("Total Seats Available", String.valueOf(totalSeats)));
        kpiPanel.add(createKpiCard("Registered Passengers", String.valueOf(totalPassengers)));

        content.add(kpiPanel, BorderLayout.NORTH);
        return content;
    }

    private JPanel createUsersPanel() {
        JPanel usersPanel = new JPanel();
        usersPanel.setLayout(new GridLayout(0, 2, 20, 20));
        usersPanel.setBackground(Color.WHITE);
        usersPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        List<User> users = UserManager.getInstance().getAllUsers();

        for (User user : users) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(new Color(245, 250, 255));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    new EmptyBorder(15, 15, 15, 15)
            ));

            JLabel name = new JLabel(user.getFirstName() + " " + user.getLastName());
            JLabel email = new JLabel("Email: " + user.getEmail());
            JLabel phone = new JLabel("Phone: " + user.getPhone());
            JLabel role = new JLabel("Role: " + user.getRole());

            for (JLabel lbl : new JLabel[]{name, email, phone, role}) {
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                card.add(lbl);
                card.add(Box.createVerticalStrut(5));
            }

            usersPanel.add(card);
        }

        return usersPanel;
    }

    private JScrollPane wrapInScrollPane(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("⚙ Settings page (TODO: Preferences or theme settings)"));
        return panel;
    }

    private JPanel createKpiCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        valueLabel.setForeground(new Color(0, 102, 204));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(Color.DARK_GRAY);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }
}
