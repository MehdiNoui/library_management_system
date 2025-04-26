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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Login page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel userLabel = new JLabel("First Name:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String firstName = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            User authenticatedUser = authenticateUser(firstName, password);
            if (authenticatedUser != null) {
                if (authenticatedUser.getRole().equals("Admin")) {
                    mainWindow.showPanel("adminDashboard");
                } else {
                    mainWindow.setCurrentReader((Reader) authenticatedUser);
                    mainWindow.showPanel("readerDashboard");
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid credentials",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout code
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(userLabel, gbc);

        gbc.gridx = 1;
        add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(passLabel, gbc);

        gbc.gridx = 1;
        add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(loginButton, gbc);
    }

    private User authenticateUser(String firstName, String password) {
        if (library == null || library.getUsers() == null) {
            return null;
        }
        for (User user : library.getUsers()) {
            if (user.getFirstname().equalsIgnoreCase(firstName) &&
                    user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}