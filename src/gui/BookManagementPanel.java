package gui;

import model.Book;
import model.Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookManagementPanel extends JPanel {
    private Library lib = Library.getInstance();
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton restockButton;

    public BookManagementPanel() {
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
        for (Book book : lib.getBooks()) {
            tableModel.addRow(new Object[]{
                    book.getId(),
                    book.getBookName(),
                    book.getAuthorName(),
                    sdf.format(book.getPublishDate()),
                    book.getGenre(),
                    book.getStockBooks(),
                    book.getAvailableBooks()
            });
        }
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
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // Add columns
        tableModel.addColumn("ID");
        tableModel.addColumn("Book Name");
        tableModel.addColumn("Author");
        tableModel.addColumn("Publish Date");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Stock");
        tableModel.addColumn("Available");
        // Add data to table
        java.util.List<Book> books = lib.getBooks();
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getId(),
                    book.getBookName(),
                    book.getAuthorName(),
                    sdf.format(book.getPublishDate()),
                    book.getGenre(),
                    book.getStockBooks(),
                    book.getAvailableBooks()
            });
        }
        // Create table
        booksTable = new JTable(tableModel);
        // Table editing
        booksTable.setRowHeight(30);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        booksTable.getTableHeader().setBackground(new Color(240, 240, 240));
        booksTable.getTableHeader().setForeground(new Color(100, 100, 100));
        // Make the table fill the available space
        booksTable.setFillsViewportHeight(true);
        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // Other buttons
        addButton = new JButton("Add Book");
        editButton = new JButton("Edit Book");
        deleteButton = new JButton("Delete Book");
        restockButton = new JButton("Restock");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(restockButton);
        // Refresh button
        JButton refreshButton = new JButton("Refresh List");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshTable()); // Calls existing refresh logic
        buttonPanel.add(refreshButton);

        // Action listeners
        // Add button functionality + dialog down below
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBookDialog();
            }
        });
        // Edit button functionality + dialog down below
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
        // Delete functionality
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
                        String bookId = (String) tableModel.getValueAt(selectedRow, 0);
                        lib.deleteBook(bookId);
                        tableModel.removeRow(selectedRow);
                        refreshTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(BookManagementPanel.this,
                            "Please select a book to delete.",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        // Restock functionality
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
                                // Update stock and available counts in table
                                int currentStock = (int) tableModel.getValueAt(selectedRow, 5);
                                int currentAvailable = (int) tableModel.getValueAt(selectedRow, 6);
                                int newStock = currentStock + quantity;
                                int newAvailable = currentAvailable + quantity;

                                tableModel.setValueAt(newStock, selectedRow, 5);
                                tableModel.setValueAt(newAvailable, selectedRow, 6);
                                // Also update the Book object in the library
                                String bookId = (String) tableModel.getValueAt(selectedRow, 0);
                                for (Book book : lib.getBooks()) {
                                    if (book.getId().equals(bookId)) {
                                        book.setStockBooks(newStock);
                                        book.setAvailableBooks(newAvailable);
                                        refreshTable();
                                        break;
                                    }
                                }
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
        // Create main panel with BorderLayout to ensure proper expansion
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        // Use BorderLayout for the main panel to ensure it expands properly
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    // Dialog functionality
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
                    Date pubDate = dateFormat.parse(dateField.getText());
                    // Parse stock
                    int stock = Integer.parseInt(stockField.getText());
                    if (stock < 0) {
                        JOptionPane.showMessageDialog(dialog,
                                "Stock cannot be negative.",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Book newBook = new Book(
                            idField.getText(),
                            nameField.getText(),
                            authorField.getText(),
                            pubDate,
                            genreField.getText(),
                            stock
                    );
                    lib.addBook(newBook);
                    refreshTable();
                    dialog.dispose();

                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Invalid date format. Please use yyyy-MM-dd.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "ID and Stock must be numbers.",
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
                    Date publishDate = dateFormat.parse(dateField.getText());

                    // Parse stock
                    int stock = Integer.parseInt(stockField.getText());
                    if (stock < 0) {
                        JOptionPane.showMessageDialog(dialog,
                                "Stock cannot be negative.",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update the corresponding Book object
                    Book bookToEdit = lib.getBooks().get(rowIndex);
                    bookToEdit.setBookName(nameField.getText());
                    bookToEdit.setAuthorName(authorField.getText());
                    bookToEdit.setGenre(genreField.getText());
                    bookToEdit.setPublishDate(publishDate);
                    bookToEdit.setStockBooks(stock);
                    bookToEdit.setAvailableBooks(stock); // adjust if you track checked-out books separately

                    refreshTable();
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

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
