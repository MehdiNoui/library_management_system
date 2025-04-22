package gui;

import model.Book;
import model.Library;
import model.user.Admin;
import model.user.Reader;
import model.user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserManagementPanel extends JPanel {
    private Library lib = Library.getInstance();
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    public UserManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        // Create header panel
        createHeaderPanel();
        // Create table panel
        createTablePanel();
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Clear the table
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (User user : lib.getUsers()) {
            tableModel.addRow(new Object[]{
                    user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    sdf.format(user.getSignupDate()),

            });
        }
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Users Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createTablePanel() {
        // Create table model with non-editable cells
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Add columns
        tableModel.addColumn("ID");
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Signup Date");
        tableModel.addColumn("Email");

        // Add data to table
        java.util.List<User> users = lib.getUsers();
        for (User user : lib.getUsers()) {
            tableModel.addRow(new Object[]{
                    user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    sdf.format(user.getSignupDate()),
                    user.getEmail()
            });
        }

        // Create table
        usersTable = new JTable(tableModel);
        // Table editing
        usersTable.setRowHeight(30);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        usersTable.getTableHeader().setBackground(new Color(240, 240, 240));
        usersTable.getTableHeader().setForeground(new Color(100, 100, 100));
        // Make the table fill the available space
        usersTable.setFillsViewportHeight(true);
        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        addButton = new JButton("Add User");
        editButton = new JButton("Edit User");
        deleteButton = new JButton("Delete User");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Add action listeners
        // Add button functionality + dialog down below
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddUserDialog();
            }
        });
        // Edit button functionality + dialog down below
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = usersTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showEditUserDialog(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(UserManagementPanel.this,
                            "Please select a user to edit.",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        // Delete functionality
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = usersTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(
                            UserManagementPanel.this,
                            "Are you sure you want to delete this user?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        String userId = (String) tableModel.getValueAt(selectedRow, 0);
                        lib.deleteUser(userId);
                        tableModel.removeRow(selectedRow);
                        refreshTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(UserManagementPanel.this,
                            "Please select a user to delete.",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        // Create main panel with BorderLayout to ensure proper expansion
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        // Use BorderLayout for the main panel to ensure it expands properly
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    private void showAddUserDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New User", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("User ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField idField = new JTextField(20);
        formPanel.add(idField, gbc);

        // First Name Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField firstNameField = new JTextField(20);
        formPanel.add(firstNameField, gbc);

        // Last Name Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField lastNameField = new JTextField(20);
        formPanel.add(lastNameField, gbc);

        // Email Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        JTextField emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Role Field
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"reader", "admin"});
        formPanel.add(roleComboBox, gbc);

        // Signup Date Field
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Signup Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JTextField signupDateField = new JTextField(dateFormat.format(new Date()), 20);
        formPanel.add(signupDateField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate input
                if (idField.getText().isEmpty() || firstNameField.getText().isEmpty() ||
                        lastNameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                        passwordField.getPassword().length == 0 || signupDateField.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog,
                            "All fields are required.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parse date to validate format
                    Date signupDate = dateFormat.parse(signupDateField.getText());
                    // Add to table

                    User newUser;
                    if (roleComboBox.getSelectedItem().equals("admin")) {
                        newUser = new Admin(
                                idField.getText(),
                                firstNameField.getText(),
                                lastNameField.getText(),
                                emailField.getText(),
                                passwordField.getText(),
                                signupDate
                        );
                    } else {
                        newUser = new Reader(
                                idField.getText(),
                                firstNameField.getText(),
                                lastNameField.getText(),
                                emailField.getText(),
                                passwordField.getText(),
                                signupDate
                        );
                    }
                    lib.signUpUser(newUser);
                    refreshTable();
                    dialog.dispose();
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Invalid date format. Please use yyyy-MM-dd.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditUserDialog(int rowIndex) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit User", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Get user data
        String userId = (String) tableModel.getValueAt(rowIndex, 0);
        String fullName = (String) tableModel.getValueAt(rowIndex, 1);
        String[] nameParts = fullName.split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        String email = (String) tableModel.getValueAt(rowIndex, 2);
        String role = (String) tableModel.getValueAt(rowIndex, 3);
        String signupDateStr = (String) tableModel.getValueAt(rowIndex, 4);

        // ID Field (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("User ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField idField = new JTextField(userId, 20);
        idField.setEditable(false);
        formPanel.add(idField, gbc);

        // First Name Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField firstNameField = new JTextField(firstName, 20);
        formPanel.add(firstNameField, gbc);

        // Last Name Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField lastNameField = new JTextField(lastName, 20);
        formPanel.add(lastNameField, gbc);

        // Email Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        JTextField emailField = new JTextField(email, 20);
        formPanel.add(emailField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Role Field
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"reader", "admin"});
        roleComboBox.setSelectedItem(role);
        formPanel.add(roleComboBox, gbc);

        // Signup Date Field
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Signup Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        JTextField signupDateField = new JTextField(signupDateStr, 20);
        formPanel.add(signupDateField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate input
                if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                        emailField.getText().isEmpty() || signupDateField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "All fields except password are required.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parse date to validate format
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.parse(signupDateField.getText());

                    // Update table
                    tableModel.setValueAt(firstNameField.getText() + " " + lastNameField.getText(), rowIndex, 1);
                    tableModel.setValueAt(emailField.getText(), rowIndex, 2);
                    tableModel.setValueAt(roleComboBox.getSelectedItem(), rowIndex, 3);
                    tableModel.setValueAt(signupDateField.getText(), rowIndex, 4);

                    // Close dialog
                    dialog.dispose();

                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Invalid date format. Please use yyyy-MM-dd.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
