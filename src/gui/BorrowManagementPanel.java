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
    private Library lib = Library.getInstance();
    private JTable borrowsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton returnButton;

    public BorrowManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        createHeaderPanel();
        createTablePanel();
        refreshTable(); // Initial table population
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Borrow borrow : lib.getBorrows()) {
            String returnDateStr = (borrow.getReturnDate() != null) ? sdf.format(borrow.getReturnDate()) : "-";
            tableModel.addRow(new Object[]{
                    borrow.getIdBorrow(),
                    borrow.getIdUser(),
                    borrow.getIdBook(),
                    sdf.format(borrow.getBorrowDate()),
                    sdf.format(borrow.getDueDate()),
                    returnDateStr,
                    borrow.getStatus()
            });
        }
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
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("ID");
        tableModel.addColumn("User");
        tableModel.addColumn("Book");
        tableModel.addColumn("Borrow Date");
        tableModel.addColumn("Due Date");
        tableModel.addColumn("Return Date");
        tableModel.addColumn("Status");

        borrowsTable = new JTable(tableModel);
        borrowsTable.setRowHeight(30);
        borrowsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        borrowsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        borrowsTable.getTableHeader().setBackground(new Color(240, 240, 240));
        borrowsTable.getTableHeader().setForeground(new Color(100, 100, 100));

        JScrollPane scrollPane = new JScrollPane(borrowsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        addButton = new JButton("New Borrow");
        returnButton = new JButton("Return Book");

        addButton.addActionListener(e -> showAddBorrowDialog());
        returnButton.addActionListener(e -> handleReturn());

        buttonPanel.add(addButton);
        buttonPanel.add(returnButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void handleReturn() {
        int selectedRow = borrowsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String status = (String) tableModel.getValueAt(selectedRow, 6);
            if (status.equals("returned")) {
                JOptionPane.showMessageDialog(this, "Book already returned", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Confirm return?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int borrowId = (int) tableModel.getValueAt(selectedRow, 0);
                lib.getBorrows().stream()
                        .filter(b -> b.getIdBorrow() == borrowId)
                        .findFirst()
                        .ifPresent(borrow -> {
                            borrow.setReturnDate(new Date());
                            borrow.setStatus("returned");
                            lib.returnBook(borrow);
                        });
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a borrow first", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showAddBorrowDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Borrow", true);
        dialog.setSize(500, 300);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID Field
        JTextField idField = new JTextField(20);
        idField.setText(String.valueOf(lib.getBorrows().isEmpty() ? 1001 :
                lib.getBorrows().get(lib.getBorrows().size() - 1).getIdBorrow() + 1));
        idField.setEditable(false);

        // User ComboBox
        JComboBox<String> userComboBox = new JComboBox<>();
        lib.getUsers().forEach(user ->
                userComboBox.addItem(user.getFullName() + " (" + user.getId() + ")"));

        // Book ComboBox
        JComboBox<String> bookComboBox = new JComboBox<>();
        lib.getBooks().forEach(book ->
                bookComboBox.addItem(book.getBookName() + " (" + book.getId() + ")"));

        // Date Fields
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JTextField borrowDateField = new JTextField(sdf.format(new Date()));
        borrowDateField.setEditable(false);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 14);
        JTextField dueDateField = new JTextField(sdf.format(cal.getTime()));

        // Form layout
        addField(formPanel, gbc, 0, "Borrow ID:", idField);
        addField(formPanel, gbc, 1, "User:", userComboBox);
        addField(formPanel, gbc, 2, "Book:", bookComboBox);
        addField(formPanel, gbc, 3, "Borrow Date:", borrowDateField);
        addField(formPanel, gbc, 4, "Due Date:", dueDateField);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        // Inside the saveButton's ActionListener:
        saveButton.addActionListener(e -> {
            try {
                String userSelection = (String) userComboBox.getSelectedItem();
                String bookSelection = (String) bookComboBox.getSelectedItem();
                String userId = userSelection.substring(userSelection.lastIndexOf("(") + 1, userSelection.length() - 1);
                String bookId = bookSelection.substring(bookSelection.lastIndexOf("(") + 1, bookSelection.length() - 1);

                // Retrieve User and Book objects
                User user = lib.getUserById(userId);
                Book book = lib.getBookById(bookId);

                if (user == null || book == null) {
                    JOptionPane.showMessageDialog(dialog, "Invalid user or book", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Use the user's borrow strategy to validate the borrow
                if (!user.borrow(book)) {
                    String errorMsg = (user.getRole().equals("Reader"))
                            ? "Reader cannot borrow (limit reached or book unavailable)"
                            : "Book not available";
                    JOptionPane.showMessageDialog(dialog, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Proceed to create the borrow record
                Borrow borrow = new Borrow(
                        Integer.parseInt(idField.getText()),
                        bookId,
                        userId,
                        sdf.parse(borrowDateField.getText()),
                        sdf.parse(dueDateField.getText())
                );

                lib.borrowForUser(borrow);
                refreshTable();
                dialog.dispose();
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);

        gbc.weightx = 0.0;
    }
}