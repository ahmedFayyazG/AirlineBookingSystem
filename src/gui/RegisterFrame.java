package gui;

import managers.UserManager;
import models.User;

import javax.swing.*;
import java.awt.*;

/**
 * DESIGN PATTERNS USED:
 * ---------------------
 * ✅ MVC: Combines View and Controller logic
 * ✅ Singleton: UserManager.getInstance()
 * ✅ (Optional) Factory for creating User object by role
 */
public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("Register - Emirates Airline");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // FULL SCREEN
        setLayout(new BorderLayout());

        // === Main Panel with centered form ===
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 248, 255));
        add(centerPanel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new Dimension(500, 500));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel title = new JLabel("✈ Register New Account", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formPanel.add(title);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        JTextField firstName = createInputField("First Name", labelFont, inputFont, formPanel);
        JTextField lastName = createInputField("Last Name", labelFont, inputFont, formPanel);
        JTextField email = createInputField("Email", labelFont, inputFont, formPanel);
        JTextField phone = createInputField("Phone", labelFont, inputFont, formPanel);
        JTextField username = createInputField("Username", labelFont, inputFont, formPanel);
        JPasswordField password = new JPasswordField();
        addLabeledField("Password", password, labelFont, inputFont, formPanel);

        // === Buttons ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(0, 123, 153));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setPreferredSize(new Dimension(120, 40));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(204, 0, 0));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(120, 40));

        buttonPanel.add(registerBtn);
        buttonPanel.add(cancelBtn);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(buttonPanel);

        // === Center the form panel ===
        centerPanel.add(formPanel);

        // === Button Logic ===
        registerBtn.addActionListener(e -> {
            User user = new User(
                0,
                firstName.getText(),
                lastName.getText(),
                email.getText(),
                phone.getText(),
                username.getText(),
                new String(password.getPassword()),
                "passenger"
            );

            boolean success = UserManager.getInstance().registerUser(user);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Account created successfully!");
                dispose();
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to create account.");
            }
        });

        cancelBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    private JTextField createInputField(String label, Font labelFont, Font fieldFont, JPanel container) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(labelFont);
        JTextField field = new JTextField();
        field.setFont(fieldFont);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        container.add(lbl);
        container.add(field);
        container.add(Box.createVerticalStrut(10));
        return field;
    }

    private void addLabeledField(String label, JComponent field, Font labelFont, Font fieldFont, JPanel container) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(labelFont);
        if (field instanceof JTextField) {
            ((JTextField) field).setFont(fieldFont);
        }
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        container.add(lbl);
        container.add(field);
        container.add(Box.createVerticalStrut(10));
    }
}


