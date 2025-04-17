package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainDashboard extends JPanel {
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private CardLayout cardLayout;

    // Dashboard panels
    private DashboardHomePanel homePanel;
    private BookManagementPanel bookPanel;
    private UserManagementPanel userPanel;
    private BorrowManagementPanel borrowPanel;

    // Colors
    private final Color SIDEBAR_BG = new Color(47, 53, 66);
    private final Color SIDEBAR_ITEM_HOVER = new Color(86, 101, 115);
    private final Color SIDEBAR_SELECTED = new Color(0, 151, 167);
    private final Color TEXT_COLOR = new Color(236, 240, 241);

    public MainDashboard() {
        setLayout(new BorderLayout());

        // Initialize panels
        createSidebar();
        createContentPanel();

        // Add panels to main dashboard
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));

        // Logo/Title panel
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setMaximumSize(new Dimension(250, 100));

        JLabel titleLabel = new JLabel("Library Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoPanel.add(titleLabel, BorderLayout.CENTER);

        // Add menu items
        sidebarPanel.add(logoPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(createSidebarItem("Dashboard", "home", true));
        sidebarPanel.add(createSidebarItem("Books Management", "books", false));
        sidebarPanel.add(createSidebarItem("Users Management", "users", false));
        sidebarPanel.add(createSidebarItem("Borrows Management", "borrows", false));

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

        logoutPanel.add(logoutButton, BorderLayout.CENTER);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        sidebarPanel.add(logoutPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JPanel createSidebarItem(String text, String cardName, boolean isSelected) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(isSelected ? SIDEBAR_SELECTED : SIDEBAR_BG);
        itemPanel.setMaximumSize(new Dimension(250, 50));

        JLabel itemLabel = new JLabel(text);
        itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        itemLabel.setForeground(TEXT_COLOR);
        itemLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemPanel.add(itemLabel, BorderLayout.CENTER);

        // Add hover effect and click action
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (itemPanel.getBackground() != SIDEBAR_SELECTED) {
                    itemPanel.setBackground(SIDEBAR_ITEM_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (itemPanel.getBackground() != SIDEBAR_SELECTED) {
                    itemPanel.setBackground(SIDEBAR_BG);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Reset all sidebar items background
                for (Component component : sidebarPanel.getComponents()) {
                    if (component instanceof JPanel) {
                        component.setBackground(SIDEBAR_BG);
                    }
                }

                // Set selected item background
                itemPanel.setBackground(SIDEBAR_SELECTED);

                // Show corresponding panel
                cardLayout.show(contentPanel, cardName);
            }
        });

        return itemPanel;
    }

    private void createContentPanel() {
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Initialize panels
        homePanel = new DashboardHomePanel();
        bookPanel = new BookManagementPanel();
        userPanel = new UserManagementPanel();
        borrowPanel = new BorrowManagementPanel();

        // Add panels to card layout
        contentPanel.add(homePanel, "home");
        contentPanel.add(bookPanel, "books");
        contentPanel.add(userPanel, "users");
        contentPanel.add(borrowPanel, "borrows");

        // Show home panel by default
        cardLayout.show(contentPanel, "home");
    }
}
