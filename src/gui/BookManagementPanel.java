package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookManagementPanel extends JPanel {
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton restockButton;

    // Static data for books
    private static String[] bookIds = {"B001", "B002", "B003", "B004", "B005"};
    private static String[] bookNames = {"The Great Gatsby", "To Kill a Mockingbird", "1984", "Pride and Prejudice", "The Hobbit"};
    private static String[] authorNames = {"F. Scott Fitzgerald", "Harper Lee", "George Orwell", "Jane Austen", "J.R.R. Tolkien"};
    private static String[] publishDates = {"1925-04-10", "1960-07-11", "1949-06-08", "1813-01-28", "1937-09-21"};
    private static String[] genres = {"Fiction", "Fiction", "Science Fiction", "Romance", "Fantasy"};
    private static int[] stockBooks = {10, 8, 12, 6, 15};
    private static int[] availableBooks = {10, 8, 12, 6, 15};

    public BookManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // Create header panel
        createHeaderPanel();

        // Create table panel
        createTablePanel();
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Books Management");
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

        // Add columns
        tableModel.addColumn("ID");
        tableModel.addColumn("Book Name");
        tableModel.addColumn("Author");
        tableModel.addColumn("Publish Date");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Stock");
        tableModel.addColumn("Available");

        // Add data to table
        for (int i = 0; i < bookIds.length; i++) {
            tableModel.addRow(new Object[]{
                    bookIds[i],
                    bookNames[i],
                    authorNames[i],
                    publishDates[i],
                    genres[i],
                    stockBooks[i],
                    availableBooks[i]
            });
        }

        // Create table
        booksTable = new JTable(tableModel);
        booksTable.setRowHeight(30);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        booksTable.getTableHeader().setBackground(new Color(240, 240, 240));
        booksTable.getTableHeader().setForeground(new Color(100, 100, 100));

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        addButton = new JButton("Add Book");
        editButton = new JButton("Edit Book");
        deleteButton = new JButton("Delete Book");
        restockButton = new JButton("Restock");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(restockButton);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBookDialog();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = booksTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showEditBookDialog(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(BookManagementPanel.this,
                            "Please select a book to edit.",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = booksTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(
                            BookManagementPanel.this,
                            "Are you sure you want to delete this book?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Remove the row from the table
                        tableModel.removeRow(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(BookManagementPanel.this,
                            "Please select a book to delete.",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        restockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = booksTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String input = JOptionPane.showInputDialog(
                            BookManagementPanel.this,
                            "Enter quantity to add to stock:",
                            "Restock Book",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (input != null && !input.isEmpty()) {
                        try {
                            int quantity = Integer.parseInt(input);
                            if (quantity > 0) {
                                // Update stock and available counts
                                int currentStock = (int) tableModel.getValueAt(selectedRow, 5);
                                int currentAvailable = (int) tableModel.getValueAt(selectedRow, 6);

                                tableModel.setValueAt(currentStock + quantity, selectedRow, 5);
                                tableModel.setValueAt(currentAvailable + quantity, selectedRow, 6);

                                JOptionPane.showMessageDialog(
                                        BookManagementPanel.this,
                                        "Successfully restocked " + quantity + " copies.",
                                        "Restock Successful",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                            } else {
                                JOptionPane.showMessageDialog(
                                        BookManagementPanel.this,
                                        "Please enter a positive number.",
                                        "Invalid Input",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(
                                    BookManagementPanel.this,
                                    "Please enter a valid number.",
                                    "Invalid Input",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            BookManagementPanel.this,
                            "Please select a book to restock.",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void showAddBookDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Book", true);
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
        formPanel.add(new JLabel("Book ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField idField = new JTextField(20);
        formPanel.add(idField, gbc);

        // Book Name Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Book Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Author Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Author:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField authorField = new JTextField(20);
        formPanel.add(authorField, gbc);

        // Publish Date Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Publish Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        JTextField dateField = new JTextField(20);
        formPanel.add(dateField, gbc);

        // Genre Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Genre:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        JTextField genreField = new JTextField(20);
        formPanel.add(genreField, gbc);

        // Stock Field
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Initial Stock:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        JTextField stockField = new JTextField(20);
        formPanel.add(stockField, gbc);

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
                if (idField.getText().isEmpty() || nameField.getText().isEmpty() ||
                        authorField.getText().isEmpty() || dateField.getText().isEmpty() ||
                        genreField.getText().isEmpty() || stockField.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog,
                            "All fields are required.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parse date to validate format
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.parse(dateField.getText());

                    // Parse stock
                    int stock = Integer.parseInt(stockField.getText());

                    if (stock < 0) {
                        JOptionPane.showMessageDialog(dialog,
                                "Stock cannot be negative.",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Add to table
                    tableModel.addRow(new Object[]{
                            idField.getText(),
                            nameField.getText(),
                            authorField.getText(),
                            dateField.getText(),
                            genreField.getText(),
                            stock,
                            stock
                    });

                    // Close dialog
                    dialog.dispose();

                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Invalid date format. Please use yyyy-MM-dd.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Stock must be a number.",
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

    private void showEditBookDialog(int rowIndex) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Book", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID Field (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Book ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField idField = new JTextField((String) tableModel.getValueAt(rowIndex, 0), 20);
        idField.setEditable(false);
        formPanel.add(idField, gbc);

        // Book Name Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Book Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField nameField = new JTextField((String) tableModel.getValueAt(rowIndex, 1), 20);
        formPanel.add(nameField, gbc);

        // Author Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Author:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField authorField = new JTextField((String) tableModel.getValueAt(rowIndex, 2), 20);
        formPanel.add(authorField, gbc);

        // Publish Date Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Publish Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        JTextField dateField = new JTextField((String) tableModel.getValueAt(rowIndex, 3), 20);
        formPanel.add(dateField, gbc);

        // Genre Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Genre:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        JTextField genreField = new JTextField((String) tableModel.getValueAt(rowIndex, 4), 20);
        formPanel.add(genreField, gbc);

        // Stock Field
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Stock:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        JTextField stockField = new JTextField(String.valueOf(tableModel.getValueAt(rowIndex, 5)), 20);
        formPanel.add(stockField, gbc);

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
                if (nameField.getText().isEmpty() || authorField.getText().isEmpty() ||
                        dateField.getText().isEmpty() || genreField.getText().isEmpty() ||
                        stockField.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog,
                            "All fields are required.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parse date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.parse(dateField.getText());

                    // Parse stock
                    int stock = Integer.parseInt(stockField.getText());

                    if (stock < 0) {
                        JOptionPane.showMessageDialog(dialog,
                                "Stock cannot be negative.",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update table
                    tableModel.setValueAt(nameField.getText(), rowIndex, 1);
                    tableModel.setValueAt(authorField.getText(), rowIndex, 2);
                    tableModel.setValueAt(dateField.getText(), rowIndex, 3);
                    tableModel.setValueAt(genreField.getText(), rowIndex, 4);
                    tableModel.setValueAt(stock, rowIndex, 5);
                    tableModel.setValueAt(stock, rowIndex, 6); // Simplified for this example

                    // Close dialog
                    dialog.dispose();

                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Invalid date format. Please use yyyy-MM-dd.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Stock must be a number.",
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
