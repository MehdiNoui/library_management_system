package gui;

import model.Book;
import model.Borrow;
import model.Library;
import model.user.Reader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderDashboard extends JPanel {
    private Reader reader;
    private Library library;
    private JTable borrowedBooksTable;
    private JTable favoriteBooksTable;
    private DefaultTableModel borrowedBooksModel;
    private DefaultTableModel favoriteBooksModel;
    private JLabel borrowLimitWeekLabel;
    private JLabel borrowLimitMonthLabel;
    private JLabel activeBorrowsLabel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Colors
    private final Color SIDEBAR_BG = new Color(47, 53, 66);
    private final Color SIDEBAR_ITEM_HOVER = new Color(86, 101, 115);
    private final Color SIDEBAR_SELECTED = new Color(0, 151, 167);
    private final Color TEXT_COLOR = new Color(236, 240, 241);
    private final Color CONTENT_BG = new Color(245, 245, 250);
    private final Color HEADER_BG = Color.WHITE;
    private final Color CARD_BG = Color.WHITE;
    private final Color CARD_BORDER = new Color(230, 230, 230);

    private JTabbedPane tabbedPane;

    public ReaderDashboard(Reader reader) {
        this.reader = reader;
        this.library = Library.getInstance();

        // Set up the panel
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(900, 700));
        setBackground(CONTENT_BG);

        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create sidebar
        JPanel sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(CONTENT_BG);
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Add header panel
        contentPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Create tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.addTab("Borrowed Books", createBorrowedBooksPanel());
        tabbedPane.addTab("Favorite Books", createFavoriteBooksPanel());
        tabbedPane.addTab("Available Books", createAvailableBooksPanel());

        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add content panel to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add the main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }

    public ReaderDashboard() {
        // Default constructor
        setLayout(new BorderLayout());
        setBackground(CONTENT_BG);
        library = Library.getInstance();

        // Initialize panels
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create sidebar
        JPanel sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(CONTENT_BG);
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Add header panel
        contentPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Create tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.addTab("Borrowed Books", createBorrowedBooksPanel());
        tabbedPane.addTab("Favorite Books", createFavoriteBooksPanel());
        tabbedPane.addTab("Available Books", createAvailableBooksPanel());

        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add content panel to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add the main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));

        // This ensures the sidebar maintains its width when the frame is resized
        sidebarPanel.setMinimumSize(new Dimension(250, 0));
        sidebarPanel.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));

        // Logo/Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(SIDEBAR_BG);
        titlePanel.setMaximumSize(new Dimension(250, 100));
        titlePanel.setMinimumSize(new Dimension(250, 100));
        titlePanel.setPreferredSize(new Dimension(250, 100));

        JLabel titleLabel = new JLabel("Library Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // User profile panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(SIDEBAR_BG);
        userPanel.setMaximumSize(new Dimension(250, 150));
        userPanel.setMinimumSize(new Dimension(250, 150));
        userPanel.setPreferredSize(new Dimension(250, 150));

        // User avatar
        JPanel avatarPanel = new JPanel();
        avatarPanel.setBackground(SIDEBAR_BG);
        avatarPanel.setPreferredSize(new Dimension(80, 80));
        avatarPanel.setLayout(new BorderLayout());

        // Create a circular avatar with user's initials
        String initials = reader != null ? getInitials(reader.getFirstname(), reader.getLastname()) : "R";
        JLabel avatarLabel = new JLabel(initials) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 151, 167));
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        avatarPanel.add(avatarLabel, BorderLayout.CENTER);

        // User info
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(SIDEBAR_BG);

        JLabel nameLabel = new JLabel(reader != null ? reader.getFirstname() + " " + reader.getLastname() : "Reader");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel(reader != null ? reader.getEmail() : "reader@example.com");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        emailLabel.setForeground(TEXT_COLOR);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userInfoPanel.add(Box.createVerticalStrut(10));
        userInfoPanel.add(nameLabel);
        userInfoPanel.add(Box.createVerticalStrut(5));
        userInfoPanel.add(emailLabel);
        userInfoPanel.add(Box.createVerticalStrut(10));

        // Add components to user panel
        userPanel.add(avatarPanel, BorderLayout.NORTH);
        userPanel.add(userInfoPanel, BorderLayout.CENTER);

        // Add menu items
        sidebarPanel.add(titlePanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(userPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create sidebar menu items
        JPanel borrowedBooksItem = createSidebarItem("Borrowed Books", true);
        JPanel favoriteBooksItem = createSidebarItem("Favorite Books", false);
        JPanel availableBooksItem = createSidebarItem("Available Books", false);

        // Add click listeners to switch tabs
        borrowedBooksItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabbedPane.setSelectedIndex(0);
                updateSidebarSelection(borrowedBooksItem);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (borrowedBooksItem.getBackground() != SIDEBAR_SELECTED) {
                    borrowedBooksItem.setBackground(SIDEBAR_ITEM_HOVER);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (borrowedBooksItem.getBackground() != SIDEBAR_SELECTED) {
                    borrowedBooksItem.setBackground(SIDEBAR_BG);
                }
            }
        });

        favoriteBooksItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabbedPane.setSelectedIndex(1);
                updateSidebarSelection(favoriteBooksItem);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (favoriteBooksItem.getBackground() != SIDEBAR_SELECTED) {
                    favoriteBooksItem.setBackground(SIDEBAR_ITEM_HOVER);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (favoriteBooksItem.getBackground() != SIDEBAR_SELECTED) {
                    favoriteBooksItem.setBackground(SIDEBAR_BG);
                }
            }
        });

        availableBooksItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabbedPane.setSelectedIndex(2);
                updateSidebarSelection(availableBooksItem);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (availableBooksItem.getBackground() != SIDEBAR_SELECTED) {
                    availableBooksItem.setBackground(SIDEBAR_ITEM_HOVER);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (availableBooksItem.getBackground() != SIDEBAR_SELECTED) {
                    availableBooksItem.setBackground(SIDEBAR_BG);
                }
            }
        });

        sidebarPanel.add(borrowedBooksItem);
        sidebarPanel.add(favoriteBooksItem);
        sidebarPanel.add(availableBooksItem);

        // Add filler to push everything to the top
        sidebarPanel.add(Box.createVerticalGlue());

        // Add logout button at bottom
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setBackground(SIDEBAR_BG);
        logoutPanel.setMaximumSize(new Dimension(250, 60));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setForeground(TEXT_COLOR);
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            // In a real application, you would handle logout logic
            JOptionPane.showMessageDialog(this, "Logged out successfully", "Logout", JOptionPane.INFORMATION_MESSAGE);

            // Find the parent window and close it
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        });

        logoutPanel.add(logoutButton, BorderLayout.CENTER);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        sidebarPanel.add(logoutPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        return sidebarPanel;
    }

    private void updateSidebarSelection(JPanel selectedItem) {
        // Get the sidebar panel
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel mainPanel = (JPanel) component;
                Component[] mainComponents = mainPanel.getComponents();
                for (Component mainComponent : mainComponents) {
                    if (mainComponent instanceof JPanel) {
                        JPanel sidebarPanel = (JPanel) mainComponent;
                        if (sidebarPanel.getLayout() instanceof BoxLayout) {
                            // Found the sidebar panel
                            Component[] sidebarItems = sidebarPanel.getComponents();
                            for (Component item : sidebarItems) {
                                if (item instanceof JPanel && item != selectedItem) {
                                    // Check if it's a menu item (not title, user panel, or logout)
                                    if (((JPanel) item).getComponentCount() > 0 &&
                                            ((JPanel) item).getComponent(0) instanceof JLabel) {
                                        JLabel label = (JLabel) ((JPanel) item).getComponent(0);
                                        String text = label.getText();
                                        if (text != null && (text.equals("Borrowed Books") ||
                                                text.equals("Favorite Books") ||
                                                text.equals("Available Books"))) {
                                            item.setBackground(SIDEBAR_BG);
                                        }
                                    }
                                }
                            }
                            // Set the selected item background
                            selectedItem.setBackground(SIDEBAR_SELECTED);
                            break;
                        }
                    }
                }
            }
        }
    }

    private String getInitials(String firstName, String lastName) {
        StringBuilder initials = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            initials.append(firstName.charAt(0));
        }
        if (lastName != null && !lastName.isEmpty()) {
            initials.append(lastName.charAt(0));
        }
        return initials.toString().toUpperCase();
    }

    private JPanel createSidebarItem(String text, boolean isSelected) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(isSelected ? SIDEBAR_SELECTED : SIDEBAR_BG);
        itemPanel.setMaximumSize(new Dimension(250, 50));
        itemPanel.setMinimumSize(new Dimension(250, 50));
        itemPanel.setPreferredSize(new Dimension(250, 50));

        JLabel itemLabel = new JLabel(text);
        itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        itemLabel.setForeground(TEXT_COLOR);
        itemLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemPanel.add(itemLabel, BorderLayout.CENTER);

        return itemPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BORDER),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + (reader != null ? reader.getFirstname() : "Reader"));
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        // User info panel
        JPanel userInfoPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        userInfoPanel.setBackground(HEADER_BG);

        // Create info cards
        JPanel weeklyLimitCard = createInfoCard("Weekly Limit", "5 books", new Color(52, 152, 219));
        JPanel monthlyLimitCard = createInfoCard("Monthly Limit", "15 books", new Color(46, 204, 113));

        // Count active borrows
        long activeBorrows = 0;
        if (library != null && reader != null) {
            activeBorrows = library.getBorrows().stream()
                    .filter(b -> b.getIdUser().equals(reader.getId()) && b.getReturnDate() == null)
                    .count();
        }

        JPanel activeBorrowsCard = createInfoCard("Currently Borrowed", activeBorrows + " books", new Color(155, 89, 182));

        // Store references to labels for later updates
        borrowLimitWeekLabel = (JLabel) weeklyLimitCard.getComponent(1);
        borrowLimitMonthLabel = (JLabel) monthlyLimitCard.getComponent(1);
        activeBorrowsLabel = (JLabel) activeBorrowsCard.getComponent(1);

        userInfoPanel.add(weeklyLimitCard);
        userInfoPanel.add(monthlyLimitCard);
        userInfoPanel.add(activeBorrowsCard);

        headerPanel.add(userInfoPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createInfoCard(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(color);

        JPanel colorBar = new JPanel();
        colorBar.setBackground(color);
        colorBar.setPreferredSize(new Dimension(panel.getWidth(), 5));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.add(colorBar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBorrowedBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CONTENT_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create table model for borrowed books
        String[] columnNames = {"Book ID", "Title", "Author", "Genre", "Borrow Date", "Due Date", "Status"};
        borrowedBooksModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Create table
        borrowedBooksTable = new JTable(borrowedBooksModel);
        borrowedBooksTable.setRowHeight(30);
        borrowedBooksTable.getTableHeader().setReorderingAllowed(false);
        borrowedBooksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        borrowedBooksTable.getTableHeader().setBackground(new Color(240, 240, 240));
        borrowedBooksTable.getTableHeader().setForeground(new Color(100, 100, 100));
        borrowedBooksTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowedBooksTable.setShowGrid(false);
        borrowedBooksTable.setIntercellSpacing(new Dimension(0, 0));
        borrowedBooksTable.setFillsViewportHeight(true);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Create a panel to hold the scroll pane with a white background and border
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        // Add action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(CONTENT_BG);

        JButton returnButton = new JButton("Return Selected Book");
        returnButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        returnButton.setBackground(new Color(231, 76, 60));
        returnButton.setForeground(Color.WHITE);
        returnButton.setBorderPainted(false);
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(e -> {
            int selectedRow = borrowedBooksTable.getSelectedRow();
            if (selectedRow >= 0) {
                String bookId = (String) borrowedBooksModel.getValueAt(selectedRow, 0);

                // Find the borrow record
                List<Borrow> userBorrows = library.getBorrows().stream()
                        .filter(b -> b.getIdUser().equals(reader.getId()) &&
                                b.getIdBook().equals(bookId) &&
                                b.getReturnDate() == null)
                        .collect(Collectors.toList());

                if (!userBorrows.isEmpty()) {
                    Borrow borrow = userBorrows.get(0);
                    borrow.setReturnDate(new Date());
                    library.returnBook(borrow);

                    JOptionPane.showMessageDialog(this,
                            "Book '" + library.getBookById(bookId).getBookName() + "' has been returned.",
                            "Book Returned",
                            JOptionPane.INFORMATION_MESSAGE);

                    loadBorrowedBooks(); // Refresh the list
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select a book to return.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton refreshButton = new JButton("Refresh List");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadBorrowedBooks());

        actionPanel.add(returnButton);
        actionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        actionPanel.add(refreshButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        // Load borrowed books
        loadBorrowedBooks();

        return panel;
    }

    private JPanel createFavoriteBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CONTENT_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(CARD_BG);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel infoLabel = new JLabel("<html>You will be notified by email when your favorite out-of-stock books become available.<br>" +
                "Add books to your favorites to receive notifications.</html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.NORTH);

        // Create table model for favorite books
        String[] columnNames = {"Book ID", "Title", "Author", "Genre", "Status"};
        favoriteBooksModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Create table
        favoriteBooksTable = new JTable(favoriteBooksModel);
        favoriteBooksTable.setRowHeight(30);
        favoriteBooksTable.getTableHeader().setReorderingAllowed(false);
        favoriteBooksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        favoriteBooksTable.getTableHeader().setBackground(new Color(240, 240, 240));
        favoriteBooksTable.getTableHeader().setForeground(new Color(100, 100, 100));
        favoriteBooksTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        favoriteBooksTable.setShowGrid(false);
        favoriteBooksTable.setIntercellSpacing(new Dimension(0, 0));
        favoriteBooksTable.setFillsViewportHeight(true);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(favoriteBooksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Create a panel to hold the scroll pane with a white background and border
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        // Add action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(CONTENT_BG);

        JButton addButton = new JButton("Add Favorite Book");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> {
            // Show a dialog with available books
            String[] bookOptions = library.getBooks().stream()
                    .filter(book -> book.getAvailableBooks() == 0) // Only out of stock books
                    .map(Book::toString)
                    .toArray(String[]::new);

            if (bookOptions.length == 0) {
                JOptionPane.showMessageDialog(this,
                        "There are no out-of-stock books to add to favorites.",
                        "No Books Available",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String selectedBook = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a book to add to favorites:",
                    "Add Favorite Book",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    bookOptions,
                    bookOptions[0]
            );

            if (selectedBook != null) {
                // Find the book in the library
                Book book = null;
                for (Book b : library.getBooks()) {
                    if (b.toString().equals(selectedBook)) {
                        book = b;
                        break;
                    }
                }

                if (book != null) {
                    // Add to favorites (in a real app, you'd store this in a database)
                    // For now, we'll just add it to the table
                    library.attach(reader); // Register for notifications

                    addFavoriteBook(book.getId(), book.getBookName(), book.getAuthorName(),
                            book.getGenre(), "Out of Stock");

                    JOptionPane.showMessageDialog(this,
                            "Book added to favorites. You will be notified when it becomes available.",
                            "Book Added",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton removeButton = new JButton("Remove Selected Book");
        removeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        removeButton.setBackground(new Color(231, 76, 60));
        removeButton.setForeground(Color.WHITE);
        removeButton.setBorderPainted(false);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(e -> {
            int selectedRow = favoriteBooksTable.getSelectedRow();
            if (selectedRow >= 0) {
                String bookId = (String) favoriteBooksModel.getValueAt(selectedRow, 0);

                // In a real app, you'd remove from the database
                // For now, we'll just remove from the table
                favoriteBooksModel.removeRow(selectedRow);

                // Detach from notifications if no more favorites
                if (favoriteBooksModel.getRowCount() == 0) {
                    library.detach(reader);
                }

                JOptionPane.showMessageDialog(this,
                        "Book removed from favorites.",
                        "Book Removed",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select a book to remove.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        actionPanel.add(addButton);
        actionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        actionPanel.add(removeButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        // Load favorite books
        loadFavoriteBooks();

        return panel;
    }

    private JPanel createAvailableBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CONTENT_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create table model for available books
        String[] columnNames = {"Book ID", "Title", "Author", "Genre", "Available", "Total Stock"};
        DefaultTableModel availableBooksModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Create table
        JTable availableBooksTable = new JTable(availableBooksModel);
        availableBooksTable.setRowHeight(30);
        availableBooksTable.getTableHeader().setReorderingAllowed(false);
        availableBooksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        availableBooksTable.getTableHeader().setBackground(new Color(240, 240, 240));
        availableBooksTable.getTableHeader().setForeground(new Color(100, 100, 100));
        availableBooksTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        availableBooksTable.setShowGrid(false);
        availableBooksTable.setIntercellSpacing(new Dimension(0, 0));
        availableBooksTable.setFillsViewportHeight(true);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(availableBooksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Create a panel to hold the scroll pane with a white background and border
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        // Add action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(CONTENT_BG);

        JButton borrowButton = new JButton("Borrow Selected Book");
        borrowButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        borrowButton.setBackground(new Color(46, 204, 113));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setBorderPainted(false);
        borrowButton.setFocusPainted(false);
        borrowButton.addActionListener(e -> {
            int selectedRow = availableBooksTable.getSelectedRow();
            if (selectedRow >= 0) {
                String bookId = (String) availableBooksModel.getValueAt(selectedRow, 0);
                Book book = library.getBookById(bookId);

                if (book != null && book.getAvailableBooks() > 0) {
                    // Check if user can borrow more books
                    long activeBorrows = library.getBorrows().stream()
                            .filter(b -> b.getIdUser().equals(reader.getId()) && b.getReturnDate() == null)
                            .count();

                    if (activeBorrows >= 3) {
                        JOptionPane.showMessageDialog(this,
                                "You have reached your borrowing limit (3 books).",
                                "Borrowing Limit Reached",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Create a new borrow
                    Calendar cal = Calendar.getInstance();
                    Date borrowDate = cal.getTime();
                    cal.add(Calendar.DAY_OF_MONTH, 14); // Due in 14 days
                    Date dueDate = cal.getTime();

                    int borrowId = library.getBorrows().size() + 1;
                    Borrow borrow = new Borrow(borrowId, bookId, reader.getId(), borrowDate, dueDate);


                    if (reader.getBorrowStrategy().borrow(reader, book)) {
                        library.borrowForUser(borrow);

                        JOptionPane.showMessageDialog(this,
                                "Book '" + book.getBookName() + "' has been borrowed.\n" +
                                        "Due date: " + dateFormat.format(dueDate),
                                "Book Borrowed",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Refresh both tables
                        loadBorrowedBooks();
                        loadAvailableBooks(availableBooksModel);
                        updateActiveBorrowsCount();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Unable to borrow this book. You may have reached your limit.",
                                "Borrow Failed",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "This book is not available for borrowing.",
                            "Not Available",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select a book to borrow.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton refreshButton = new JButton("Refresh List");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadAvailableBooks(availableBooksModel));

        actionPanel.add(borrowButton);
        actionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        actionPanel.add(refreshButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        // Load available books
        loadAvailableBooks(availableBooksModel);

        return panel;
    }

    private void loadBorrowedBooks() {
        // Clear existing data
        borrowedBooksModel.setRowCount(0);

        // Get all borrows for this user
        List<Borrow> userBorrows = library.getBorrows().stream()
                .filter(b -> b.getIdUser().equals(reader.getId()))
                .collect(Collectors.toList());

        // Add each borrow to the table
        for (Borrow borrow : userBorrows) {
            // Find the book
            Book book = library.getBookById(borrow.getIdBook());
            if (book != null) {
                // Update borrow status
                borrow.updateStatus();

                // Add row to table
                borrowedBooksModel.addRow(new Object[]{
                        book.getId(),
                        book.getBookName(),
                        book.getAuthorName(),
                        book.getGenre(),
                        dateFormat.format(borrow.getBorrowDate()),
                        dateFormat.format(borrow.getDueDate()),
                        borrow.getStatus()
                });
            }
        }

        // Update the active borrows count
        updateActiveBorrowsCount();
    }

    private void loadFavoriteBooks() {
        // Clear existing data
        favoriteBooksModel.setRowCount(0);

        // In a real application, you would load favorites from a database
        // For demonstration, we'll add books from the library that are out of stock
        for (Book book : library.getBooks()) {
            if (book.getAvailableBooks() == 0) {
                addFavoriteBook(book.getId(), book.getBookName(), book.getAuthorName(),
                        book.getGenre(), "Out of Stock");
            }
        }
    }

    private void loadAvailableBooks(DefaultTableModel model) {
        // Clear existing data
        model.setRowCount(0);

        // Add all books from the library
        for (Book book : library.getBooks()) {
            model.addRow(new Object[]{
                    book.getId(),
                    book.getBookName(),
                    book.getAuthorName(),
                    book.getGenre(),
                    book.getAvailableBooks(),
                    book.getStockBooks()
            });
        }
    }

    private void addFavoriteBook(String id, String title, String author, String genre, String status) {
        favoriteBooksModel.addRow(new Object[]{
                id, title, author, genre, status
        });
    }

    private void updateActiveBorrowsCount() {
        if (activeBorrowsLabel == null || reader == null) {
            return; // Skip if label or reader is not initialized
        }

        long activeBorrows = library.getBorrows().stream()
                .filter(b -> b.getIdUser().equals(reader.getId()) && b.getReturnDate() == null)
                .count();

        activeBorrowsLabel.setText(activeBorrows + " books");
    }

    // Method to set the reader after construction
    public void setReader(Reader reader) {
        this.reader = reader;
        if (library == null) {
            library = Library.getInstance();
        }

        // Refresh data
        if (borrowedBooksModel != null) {
            loadBorrowedBooks();
        }
        if (favoriteBooksModel != null) {
            loadFavoriteBooks();
        }

        // Only update if the label exists
        if (activeBorrowsLabel != null) {
            updateActiveBorrowsCount();
        }
    }

    // Main method for demonstration
    public static void main(String[] args) {
        // Get the library instance (already populated with data)
        Library library = Library.getInstance();

        // Get the first reader from the library
        Reader reader = (Reader) library.getUsers().stream().filter(user -> user instanceof Reader).findFirst().orElse(null);

        if (reader == null) {
            System.err.println("No reader found in the library database.");
            return;
        }

        // Create some sample borrows if none exist
        if (library.getBorrows().isEmpty()) {
            // Create sample borrows for demonstration
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();

            cal.add(Calendar.DAY_OF_MONTH, -5);
            Date fiveDaysAgo = cal.getTime();

            cal.add(Calendar.DAY_OF_MONTH, -5);
            Date tenDaysAgo = cal.getTime();

            cal.add(Calendar.DAY_OF_MONTH, 14);
            Date dueDate1 = cal.getTime();

            cal.add(Calendar.DAY_OF_MONTH, 5);
            Date dueDate2 = cal.getTime();

            // Create borrows
            Borrow borrow1 = new Borrow(1, "b01", reader.getId(), tenDaysAgo, dueDate1);
            Borrow borrow2 = new Borrow(2, "b02", reader.getId(), fiveDaysAgo, dueDate2);

            // Add to library
            library.borrowForUser(borrow1);
            library.borrowForUser(borrow2);
        }

        // Launch the dashboard in a JFrame
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Reader Dashboard - " + reader.getFirstname() + " " + reader.getLastname());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setMinimumSize(new Dimension(900, 700));
            frame.setLocationRelativeTo(null);

            ReaderDashboard dashboard = new ReaderDashboard(reader);
            frame.add(dashboard);
            frame.setVisible(true);
        });
    }
}
