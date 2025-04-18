package gui;

import model.Borrow;
import model.Library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DashboardHomePanel extends JPanel {
    Library lib = Library.getInstance();
    private JLabel timeLabel;
    private JLabel dateLabel;
    private Timer timer;
    // Dashboard statistics
    private JLabel totalBooksLabel;
    private JLabel totalUsersLabel;
    private JLabel activeBorrowsLabel;
    private JLabel overdueBorrowsLabel;

    public DashboardHomePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        // Create header panel
        createHeaderPanel();
        // Create statistics panel
        createStatisticsPanel();
        // Create recent activities panel
        createRecentActivitiesPanel();
        // Start the timer to update time
        startTimer();
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome, Administrator");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel timePanel = new JPanel(new GridLayout(2, 1));
        timePanel.setBackground(new Color(255, 255, 255));

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        dateLabel = new JLabel();
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        updateTimeAndDate();

        timePanel.add(timeLabel);
        timePanel.add(dateLabel);

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(timePanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createStatisticsPanel() {
        JPanel statsContainer = new JPanel(new GridLayout(1, 4, 15, 0));
        statsContainer.setBackground(new Color(245, 245, 250));
        statsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Total Books
        JPanel booksPanel = createStatCard(
                "Total Books", String.valueOf(lib.getTotalBooks()),
                new Color(52, 152, 219)
        );
        // Total Users
        JPanel usersPanel = createStatCard(
                "Total Users", String.valueOf(lib.getTotalUsers()),
                new Color(46, 204, 113)
        );
        // Active Borrows
        JPanel activeBorrowsPanel = createStatCard(
                "Active Borrows", String.valueOf(lib.getActiveBorrows()),
                new Color(155, 89, 182)
        );
        // Overdue Borrows
        JPanel overduePanel = createStatCard(
                "Overdue", String.valueOf(lib.getOverdueBorrows()),
                new Color(231, 76, 60)
        );

        statsContainer.add(booksPanel);
        statsContainer.add(usersPanel);
        statsContainer.add(activeBorrowsPanel);
        statsContainer.add(overduePanel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 245, 250));
        centerPanel.add(statsContainer, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        // Shadow
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        // TitleValue
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        // TextValue
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        // TextValue
        JPanel colorBar = new JPanel();
        colorBar.setBackground(color);
        colorBar.setPreferredSize(new Dimension(panel.getWidth(), 5));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.add(colorBar, BorderLayout.SOUTH);
        return panel;
    }

    private void createRecentActivitiesPanel() {
        JPanel activitiesPanel = new JPanel(new BorderLayout());
        activitiesPanel.setBackground(Color.WHITE);
        activitiesPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 20, 20, 20),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        ));

        JLabel titleLabel = new JLabel("Recent Activities");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // Sample data for the table
        String[] columnNames = {"Activity", "User", "Book", "Date"};
        java.util.List<Borrow> borrows = Library.getInstance().getBorrows();
        Object[][] data = new Object[borrows.size()][4];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (int i = 0; i < borrows.size(); i++) {
            Borrow b = borrows.get(i);
            String activity = (b.getReturnDate() == null) ? "Book Borrowed" : "Book Returned";
            String userName = Library.getInstance().getUserById(b.getIdUser()) != null ?
                    Library.getInstance().getUserById(b.getIdUser()).getFullName() : "Unknown User";
            String bookTitle = Library.getInstance().getBookById(b.getIdBook()) != null ?
                    Library.getInstance().getBookById(b.getIdBook()).getBookName() : "Unknown Book";
            Date date = (b.getReturnDate() != null) ? b.getReturnDate() : b.getBorrowDate();

            data[i][0] = activity;
            data[i][1] = userName;
            data[i][2] = bookTitle;
            data[i][3] = sdf.format(date);
        }

        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(new Color(100, 100, 100));

        // Make the table fill the available space
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        activitiesPanel.add(titleLabel, BorderLayout.NORTH);
        activitiesPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel centerPanel = (JPanel) getComponent(1);
        centerPanel.add(activitiesPanel, BorderLayout.CENTER);
    }


    private void updateTimeAndDate() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        Date now = new Date();
        timeLabel.setText(timeFormat.format(now));
        dateLabel.setText(dateFormat.format(now));
    }
    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimeAndDate();
            }
        });
        timer.start();
    }
    // Stop the timer when the panel is no longer visible
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (timer != null) {
            timer.stop();
        }
    }
}
