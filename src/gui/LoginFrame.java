package gui;

import managers.UserManager;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Emirates Airline Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        Font baseFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);

        getContentPane().setBackground(Color.WHITE);

        // === Title ===
        JLabel title = new JLabel("Welcome to Emirates Booking Portal", JLabel.CENTER);
        title.setFont(titleFont);
        title.setForeground(new Color(150, 0, 0));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // === Form ===
        JPanel formPanel = new JPanel(null);
        formPanel.setPreferredSize(new Dimension(450, 200));
        formPanel.setBackground(Color.WHITE);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        userLabel.setBounds(50, 20, 100, 25);
        usernameField.setBounds(160, 20, 250, 30);
        passLabel.setBounds(50, 70, 100, 25);
        passwordField.setBounds(160, 70, 250, 30);

        userLabel.setFont(baseFont);
        passLabel.setFont(baseFont);
        usernameField.setFont(baseFont);
        passwordField.setFont(baseFont);

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);
        add(formPanel, BorderLayout.CENTER);

        // === Buttons ===
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        loginButton = new JButton("Login");
        loginButton.setFont(baseFont);
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton registerButton = new JButton("Create Account");
        registerButton.setFont(baseFont);
        registerButton.setBackground(new Color(0, 153, 76));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(150, 40));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // === Actions ===
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            User user = UserManager.getInstance().validateUser(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "✅ Logged in as " + user.getRole());
                dispose();

                if ("admin".equals(user.getRole())) {
                    new AdminDashboard();
                } else {
                    new PassengerDashboard(user.getId(), user.getFirstName());
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid username or password");
            }
        });

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        setVisible(true);
    }
}
