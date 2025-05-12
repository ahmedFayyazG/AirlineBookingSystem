package gui;

import managers.UserManager;
import models.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login - Emirates Booking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);

        // === Left Panel (Form) ===
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        JLabel title = new JLabel("Emirates Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // adds left margin


        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JCheckBox rememberMe = new JCheckBox("Remember Me");
        rememberMe.setAlignmentX(Component.LEFT_ALIGNMENT);
        rememberMe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rememberMe.setOpaque(false);
        rememberMe.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel forgotPassword = new JLabel("Forgot Password?");
        forgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgotPassword.setForeground(new Color(0, 102, 204));
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Buttons Row
        JButton loginBtn = new JButton("SIGN IN");
        JButton registerBtn = new JButton("Register");
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setBackground(new Color(200, 16, 46));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(100, 40));

        registerBtn.setBackground(new Color(200, 16, 46));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setFocusPainted(false);
        registerBtn.setPreferredSize(new Dimension(100, 40));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        // Add components to form panel
        formPanel.add(title);
        formPanel.add(userLabel);
        formPanel.add(Box.createVerticalStrut(10)); // adds vertical space
        
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);
        formPanel.add(rememberMe);
        formPanel.add(forgotPassword);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(buttonPanel);

        // === Right Panel (Image + Welcome) ===
        JPanel imagePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = new ImageIcon("src/assets/login-bg.jpg");
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        imagePanel.setLayout(new BorderLayout());

        JPanel overlay = new JPanel();
        overlay.setOpaque(false);
        overlay.setLayout(new BoxLayout(overlay, BoxLayout.Y_AXIS));
        overlay.setBorder(BorderFactory.createEmptyBorder(100, 60, 60, 60));

        JLabel welcome = new JLabel("Welcome to Emirates");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcome.setForeground(Color.BLACK);

        JLabel desc = new JLabel("<html><p style='width:300px'>Book your flights quickly and easily with our modern platform. Experience comfort, safety, and efficiency with Emirates.</p></html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc.setForeground(Color.LIGHT_GRAY);
        desc.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        overlay.add(welcome);
        overlay.add(desc);
        imagePanel.add(overlay, BorderLayout.CENTER);

        // === Add panels to split pane
        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(imagePanel);
        add(splitPane, BorderLayout.CENTER);

        // === Login Logic ===
        loginBtn.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            User loginUser = UserManager.getInstance().validateUser(user, pass);

            if (loginUser != null) {
                JOptionPane.showMessageDialog(this, "✅ Welcome, " + loginUser.getFirstName());
                dispose();
                if ("admin".equals(loginUser.getRole())) {
                    new AdminDashboard();
                } else {
                    new PassengerDashboard(loginUser.getId(), loginUser.getFirstName());
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        setVisible(true);
    }
}
