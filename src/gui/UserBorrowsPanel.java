package gui;

import model.user.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserBorrowsPanel extends JPanel {
    private JPanel borrowsContainer;

    // Colors
    private final Color CARD_BG = Color.WHITE;
    private final Color CARD_BORDER = new Color(230, 230, 230);
    private final Color TITLE_COLOR = new Color(47, 53, 66);
    private final Color AUTHOR_COLOR = new Color(86, 101, 115);
    private final Color ACTIVE_COLOR = new Color(52, 152, 219);
    private final Color RETURNED_COLOR = new Color(46, 204, 113);
    private final Color OVERDUE_COLOR = new Color(231, 76, 60);

    // Sample data for borrows
    private static int[] borrowIds = {1001, 1002, 1003};
    private static String[] bookIds = {"B001", "B002", "B003"};
    private static String[] bookNames = {"The Great Gatsby", "To Kill a Mockingbird", "1984"};
    private static String[] authorNames = {"F. Scott Fitzgerald", "Harper Lee", "George Orwell"};
    private static Date[] borrowDates = new Date[3];
    private static Date[] dueDates = new Date[3];
    private static Date[] returnDates = new Date[3];
    private static String[] statuses = {"active", "returned", "overdue"};

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

        // 20 days ago for third borrow
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -20);
        borrowDates[2] = cal.getTime();

        // Due date for third borrow (6 days ago)
        cal.add(Calendar.DAY_OF_MONTH, 14);
        dueDates[2] = cal.getTime();
        returnDates[2] = null;
    }

    public UserBorrowsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // Create header panel
        createHeaderPanel();

        // Create borrows panel
        createBorrowsPanel();
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Borrowed Books");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        // Create filter buttons
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

        JButton allButton = new JButton("All");
        JButton activeButton = new JButton("Active");
        JButton returnedButton = new JButton("Returned");
        JButton overdueButton = new JButton("Overdue");

        // Style the buttons
        styleFilterButton(allButton, true);
        styleFilterButton(activeButton, false);
        styleFilterButton(returnedButton, false);
        styleFilterButton(overdueButton, false);

        // Add action listeners (to be implemented)
        allButton.addActionListener(e -> {
            styleFilterButton(allButton, true);
            styleFilterButton(activeButton, false);
            styleFilterButton(returnedButton, false);
            styleFilterButton(overdueButton, false);
            // Filter logic would go here
        });

        activeButton.addActionListener(e -> {
            styleFilterButton(allButton, false);
            styleFilterButton(activeButton, true);
            styleFilterButton(returnedButton, false);
            styleFilterButton(overdueButton, false);
            // Filter logic would go here
        });

        returnedButton.addActionListener(e -> {
            styleFilterButton(allButton, false);
            styleFilterButton(activeButton, false);
            styleFilterButton(returnedButton, true);
            styleFilterButton(overdueButton, false);
            // Filter logic would go here
        });

        overdueButton.addActionListener(e -> {
            styleFilterButton(allButton, false);
            styleFilterButton(activeButton, false);
            styleFilterButton(returnedButton, false);
            styleFilterButton(overdueButton, true);
            // Filter logic would go here
        });

        filterPanel.add(allButton);
        filterPanel.add(activeButton);
        filterPanel.add(returnedButton);
        filterPanel.add(overdueButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(filterPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void styleFilterButton(JButton button, boolean isSelected) {
        if (isSelected) {
            button.setBackground(new Color(0, 151, 167));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(240, 240, 240));
            button.setForeground(new Color(100, 100, 100));
        }
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void createBorrowsPanel() {
        // Create a panel with a vertical BoxLayout for the borrows
        borrowsContainer = new JPanel();
        borrowsContainer.setLayout(new BoxLayout(borrowsContainer, BoxLayout.Y_AXIS));
        borrowsContainer.setBackground(new Color(245, 245, 250));
        borrowsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create borrow cards
        for (int i = 0; i < borrowIds.length; i++) {
            // Create borrow card
            JPanel borrowCard = createBorrowCard(
                    borrowIds[i],
                    bookIds[i],
                    bookNames[i],
                    authorNames[i],
                    borrowDates[i],
                    dueDates[i],
                    returnDates[i],
                    statuses[i]
            );

            borrowsContainer.add(borrowCard);

            // Add spacing between cards
            if (i < borrowIds.length - 1) {
                borrowsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        // Create a scroll pane for the borrows container
        JScrollPane scrollPane = new JScrollPane(borrowsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createBorrowCard(int borrowId, String bookId, String bookName, String authorName,
                                    Date borrowDate, Date dueDate, Date returnDate, String status) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(CARD_BORDER, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Left panel for book info
        JPanel bookInfoPanel = new JPanel();
        bookInfoPanel.setLayout(new BoxLayout(bookInfoPanel, BoxLayout.Y_AXIS));
        bookInfoPanel.setBackground(CARD_BG);
        bookInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        bookInfoPanel.setPreferredSize(new Dimension(300, 0));

        // Book title
        JLabel titleLabel = new JLabel(bookName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Author
        JLabel authorLabel = new JLabel("by " + authorName);
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        authorLabel.setForeground(AUTHOR_COLOR);
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Book ID
        JLabel idLabel = new JLabel("Book ID: " + bookId);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        idLabel.setForeground(AUTHOR_COLOR);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        bookInfoPanel.add(titleLabel);
        bookInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bookInfoPanel.add(authorLabel);
        bookInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bookInfoPanel.add(idLabel);

        // Center panel for borrow details
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsPanel.setBackground(CARD_BG);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

        // Borrow ID
        JLabel borrowIdLabel = new JLabel("Borrow ID:");
        borrowIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsPanel.add(borrowIdLabel);

        JLabel borrowIdValueLabel = new JLabel(String.valueOf(borrowId));
        borrowIdValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsPanel.add(borrowIdValueLabel);

        // Borrow Date
        JLabel borrowDateLabel = new JLabel("Borrowed On:");
        borrowDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsPanel.add(borrowDateLabel);

        JLabel borrowDateValueLabel = new JLabel(dateFormat.format(borrowDate));
        borrowDateValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsPanel.add(borrowDateValueLabel);

        // Due Date
        JLabel dueDateLabel = new JLabel("Due Date:");
        dueDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsPanel.add(dueDateLabel);

        JLabel dueDateValueLabel = new JLabel(dateFormat.format(dueDate));
        dueDateValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsPanel.add(dueDateValueLabel);

        // Return Date (if applicable)
        if (returnDate != null) {
            JLabel returnDateLabel = new JLabel("Returned On:");
            returnDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            detailsPanel.add(returnDateLabel);

            JLabel returnDateValueLabel = new JLabel(dateFormat.format(returnDate));
            returnDateValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            detailsPanel.add(returnDateValueLabel);
        }

        // Right panel for status and actions
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(CARD_BG);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        statusPanel.setPreferredSize(new Dimension(150, 0));

        // Status label
        JLabel statusLabel = new JLabel(status.toUpperCase());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set color based on status
        if ("active".equals(status)) {
            statusLabel.setForeground(ACTIVE_COLOR);
        } else if ("returned".equals(status)) {
            statusLabel.setForeground(RETURNED_COLOR);
        } else if ("overdue".equals(status)) {
            statusLabel.setForeground(OVERDUE_COLOR);
        }

        // Return button (if applicable)
        JButton returnButton = new JButton("Return Book");
        returnButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        returnButton.setForeground(Color.WHITE);
        returnButton.setBackground(ACTIVE_COLOR);
        returnButton.setBorderPainted(false);
        returnButton.setFocusPainted(false);
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.setMaximumSize(new Dimension(150, 40));

        if ("returned".equals(status)) {
            returnButton.setEnabled(false);
            returnButton.setText("Returned");
            returnButton.setBackground(new Color(200, 200, 200));
        } else if ("overdue".equals(status)) {
            returnButton.setBackground(OVERDUE_COLOR);
            returnButton.setText("Return Now");
        }

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Return functionality to be implemented
                JOptionPane.showMessageDialog(
                        UserBorrowsPanel.this,
                        "Book returned successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        statusPanel.add(Box.createVerticalGlue());
        statusPanel.add(statusLabel);
        statusPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        statusPanel.add(returnButton);
        statusPanel.add(Box.createVerticalGlue());

        // Add all panels to the card
        card.add(bookInfoPanel, BorderLayout.WEST);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.EAST);

        return card;
    }
}
