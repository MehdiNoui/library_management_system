package gui;

import model.Book;
import model.Borrow;
import model.Library;
import model.user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BorrowManagementPanel extends JPanel {
    private JTable borrowsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton returnButton;

    // Static data for borrows
    private static int[] borrowIds = {1001, 1002, 1003};
    private static String[] bookIds = {"B001", "B002", "B003"};
    private static String[] userIds = {"U001", "U002", "U003"};
    private static String[] bookNames = {"The Great Gatsby", "To Kill a Mockingbird", "1984"};
    private static String[] userNames = {"John Doe", "Jane Smith", "Mike Johnson"};
    private static Date[] borrowDates = new Date[3];
    private static Date[] dueDates = new Date[3];
    private static Date[] returnDates = new Date[3];
    private static String[] statuses = {"active", "returned", "active"};

    // Static data for available books and users (for dropdown)
    private static String[] availableBookIds = {"B001", "B002", "B003", "B004", "B005"};
    private static String[] availableBookNames = {"The Great Gatsby", "To Kill a Mockingbird", "1984", "Pride and Prejudice", "The Hobbit"};
    private static String[] availableUserIds = {"U001", "U002", "U003", "U004", "U005"};
    private static String[] availableUserNames = {"John Doe", "Jane Smith", "Mike Johnson", "Alice Brown", "Admin User"};

    // Library instance
    private Library library = Library.getInstance();

    static {
        // Initialize dates
        Calendar cal = Calendar.getInstance();

        // Current date for first borrow
        borrowDates[0] = cal.getTime();

        // Due date for first borrow (14 days later)
        cal.add(Calendar.DAY_OF_MONTH, 14);
        dueDates[0] = cal.getTime();
        returnDates[0] = null;

        // 7 days ago for second borrow
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        borrowDates[1] = cal.getTime();

        // Due date for second borrow (7 days from now)
        cal.add(Calendar.DAY_OF_MONTH, 14);
        dueDates[1] = cal.getTime();

        // Return date for second borrow (today)
        returnDates[1] = new Date();

        // 3 days ago for third borrow
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -3);
        borrowDates[2] = cal.getTime();

        // Due date for third borrow (11 days from now)
        cal.add(Calendar.DAY_OF_MONTH, 14);
        dueDates[2] = cal.getTime();
        returnDates[2] = null;

        // Initialize library with sample data
        initializeLibraryData();
    }

    private static void initializeLibraryData() {
        Library library = Library.getInstance();

        // Add sample borrows to library
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < borrowIds.length; i++) {
            Borrow borrow = new Borrow(borrowIds[i], bookIds[i], userIds[i], borrowDates[i], dueDates[i]);
            if (returnDates[i] != null) {
                borrow.setReturnDate(returnDates[i]);
            }
            borrow.setStatus(statuses[i]);
            library.getBorrows().add(borrow);
        }
    }

    public BorrowManagementPanel() {
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

        JLabel titleLabel = new JLabel("Borrows Management");
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
        tableModel.addColumn("User");
        tableModel.addColumn("Book");
        tableModel.addColumn("Borrow Date");
        tableModel.addColumn("Due Date");
        tableModel.addColumn("Return Date");
        tableModel.addColumn("Status");

        // Add data to table
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < borrowIds.length; i++) {
            tableModel.addRow(new Object[]{
                    borrowIds[i],
                    userNames[i] + " (" + userIds[i] + ")",
                    bookNames[i] + " (" + bookIds[i] + ")",
                    dateFormat.format(borrowDates[i]),
                    dateFormat.format(dueDates[i]),
                    returnDates[i] != null ? dateFormat.format(returnDates[i]) : "-",
                    statuses[i]
            });
        }

        // Create table
        borrowsTable = new JTable(tableModel);
        borrowsTable.setRowHeight(30);
        borrowsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        borrowsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        borrowsTable.getTableHeader().setBackground(new Color(240, 240, 240));
        borrowsTable.getTableHeader().setForeground(new Color(100, 100, 100));

        // Make the table fill the available space
        borrowsTable.setFillsViewportHeight(true);

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(borrowsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        addButton = new JButton("New Borrow");
        returnButton = new JButton("Return Book");

        buttonPanel.add(addButton);
        buttonPanel.add(returnButton);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBorrowDialog();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = borrowsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String status = (String) tableModel.getValueAt(selectedRow, 6);

                    if (status.equals("returned")) {
                        JOptionPane.showMessageDialog(BorrowManagementPanel.this,
                                "This book has already been returned.",
                                "Already Returned",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(
                            BorrowManagementPanel.this,
                            "Confirm return of book?",
                            "Confirm Return",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Update status and return date
                        Date returnDate = new Date();

                        // Get the borrow ID
                        int borrowId = (int) tableModel.getValueAt(selectedRow, 0);

                        // Find the borrow in the library
                        for (Borrow borrow : library.getBorrows()) {
                            if (borrow.getIdBorrow() == borrowId) {
                                borrow.setReturnDate(returnDate);
                                borrow.setStatus("returned");

                                // Return the book in the library
                                library.returnBook(borrow);
                                break;
                            }
                        }

                        // Update table
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        tableModel.setValueAt(dateFormat.format(returnDate), selectedRow, 5);
                        tableModel.setValueAt("returned", selectedRow, 6);

                        JOptionPane.showMessageDialog(BorrowManagementPanel.this,
                                "Book returned successfully.",
                                "Return Successful",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(BorrowManagementPanel.this,
                            "Please select a borrow to return.",
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

    private void showAddBorrowDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Borrow", true);
        dialog.setSize(500, 300);
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
        formPanel.add(new JLabel("Borrow ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField idField = new JTextField(20);
        // Generate a new ID (last ID + 1)
        idField.setText(String.valueOf(borrowIds.length > 0 ? borrowIds[borrowIds.length - 1] + 1 : 1001));
        formPanel.add(idField, gbc);

        // User Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("User:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        JComboBox<String> userComboBox = new JComboBox<>();
        for (int i = 0; i < availableUserIds.length; i++) {
            userComboBox.addItem(availableUserNames[i] + " (" + availableUserIds[i] + ")");
        }
        formPanel.add(userComboBox, gbc);

        // Book Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Book:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        JComboBox<String> bookComboBox = new JComboBox<>();
        for (int i = 0; i < availableBookIds.length; i++) {
            bookComboBox.addItem(availableBookNames[i] + " (" + availableBookIds[i] + ")");
        }
        formPanel.add(bookComboBox, gbc);

        // Borrow Date Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Borrow Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JTextField borrowDateField = new JTextField(dateFormat.format(new Date()), 20);
        borrowDateField.setEditable(false);
        formPanel.add(borrowDateField, gbc);

        // Due Date Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Due Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 14); // Default due date is 14 days from today
        JTextField dueDateField = new JTextField(dateFormat.format(cal.getTime()), 20);
        formPanel.add(dueDateField, gbc);

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
                if (idField.getText().isEmpty() || dueDateField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "All fields are required.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parse dates
                    Date borrowDate = dateFormat.parse(borrowDateField.getText());
                    Date dueDate = dateFormat.parse(dueDateField.getText());

                    // Get selected user and book
                    String userSelection = (String) userComboBox.getSelectedItem();
                    String bookSelection = (String) bookComboBox.getSelectedItem();

                    // Extract IDs from selections
                    String userId = userSelection.substring(userSelection.lastIndexOf("(") + 1, userSelection.lastIndexOf(")"));
                    String bookId = bookSelection.substring(bookSelection.lastIndexOf("(") + 1, bookSelection.lastIndexOf(")"));

                    // Create new borrow
                    int borrowId = Integer.parseInt(idField.getText());
                    Borrow borrow = new Borrow(borrowId, bookId, userId, borrowDate, dueDate);

                    // Add to library
                    library.borrowForUser(borrow);

                    // Add to table
                    tableModel.addRow(new Object[]{
                            borrowId,
                            userSelection,
                            bookSelection,
                            dateFormat.format(borrowDate),
                            dateFormat.format(dueDate),
                            "-",
                            "active"
                    });

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
