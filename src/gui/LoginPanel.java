package gui;

import model.Library;
import model.user.Admin;
import model.user.Reader;
import model.user.User;
import window.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
    private MainWindow mainWindow;
    private Library library;

    public LoginPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.library = Library.getInstance();
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));

        // Title
        JLabel titleLabel = new JLabel("Library Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0, 102, 204));

        // Email Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                emailField.getBorder(),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        // Password Field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
                passField.getBorder(),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        // Login Button
        JButton loginButton = new JButton("Sign In");
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            User authenticatedUser = authenticateUser(email, password);
            if (authenticatedUser != null) {
                if (authenticatedUser.getRole().equals("Admin")) {
                    mainWindow.showPanel("adminDashboard");
                } else {
                    mainWindow.setCurrentReader((Reader) authenticatedUser);
                    mainWindow.showPanel("readerDashboard");
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid email or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(logoLabel, gbc);

        gbc.gridy = 1;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);
    }

    private User authenticateUser(String email, String password) {
        if (library == null || library.getUsers() == null) {
            return null;
        }
        for (User user : library.getUsers()) {
            if (user.getEmail().equalsIgnoreCase(email) &&
                    user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}