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
    private JButton refreshButton;
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
        // Buttons
        returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> handleReturn());
        refreshButton = new JButton("Refresh List");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshTable()); // Calls existing refresh logic
        //adding to panel..
        buttonPanel.add(returnButton);
        buttonPanel.add(refreshButton);
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
}