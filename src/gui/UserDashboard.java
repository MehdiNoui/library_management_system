package gui;

import model.Book;
import model.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDashboard extends JPanel {
    private User currentUser;
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private CardLayout cardLayout;

    // Dashboard panels
    private BookCatalogPanel catalogPanel;
    private UserBorrowsPanel borrowsPanel;
    private UserProfilePanel profilePanel;

    // Colors
    private final Color SIDEBAR_BG = new Color(47, 53, 66);
    private final Color SIDEBAR_ITEM_HOVER = new Color(86, 101, 115);
    private final Color SIDEBAR_SELECTED = new Color(0, 151, 167);
    private final Color TEXT_COLOR = new Color(236, 240, 241);

    // Default constructor - no arguments
    public UserDashboard() {
        setLayout(new BorderLayout());

        // Initialize panels
        createSidebar();
        createContentPanel();

        // Add panels to main dashboard
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Set minimum size to ensure components don't get too small
        setMinimumSize(new Dimension(800, 600));
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));

        // This ensures the sidebar maintains its width when the frame is resized
        sidebarPanel.setMinimumSize(new Dimension(250, 0));
        sidebarPanel.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));

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

        // Create a circular avatar with placeholder
        JLabel avatarLabel = new JLabel("JD") {
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

        JLabel nameLabel = new JLabel("John Doe");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("john.doe@example.com");
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
        sidebarPanel.add(userPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(createSidebarItem("Book Catalog", "catalog", true));
        sidebarPanel.add(createSidebarItem("My Borrows", "borrows", false));
        sidebarPanel.add(createSidebarItem("My Profile", "profile", false));

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

    private JPanel createSidebarItem(String text, String cardName, boolean isSelected) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(isSelected ? SIDEBAR_SELECTED : SIDEBAR_BG);
        itemPanel.setMaximumSize(new Dimension(250, 50));
        itemPanel.setMinimumSize(new Dimension(250, 50));
        itemPanel.setPreferredSize(new Dimension(250, 50));
        itemPanel.setName("menuItem"); // Add a name to identify menu items

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
                    // Only change background for menu items, not user panel or logout panel
                    if (component instanceof JPanel && "menuItem".equals(component.getName())) {
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

        // This ensures the content panel expands to fill available space
        contentPanel.setMinimumSize(new Dimension(550, 600));

        // Initialize panels
        catalogPanel = new BookCatalogPanel();
        borrowsPanel = new UserBorrowsPanel();
        profilePanel = new UserProfilePanel();

        // Add panels to card layout
        contentPanel.add(catalogPanel, "catalog");
        contentPanel.add(borrowsPanel, "borrows");
        contentPanel.add(profilePanel, "profile");

        // Show catalog panel by default
        cardLayout.show(contentPanel, "catalog");
    }

    // Method to switch to a specific panel
    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);

        // Update sidebar selection
        for (Component component : sidebarPanel.getComponents()) {
            if (component instanceof JPanel && "menuItem".equals(component.getName())) {
                JPanel menuItem = (JPanel) component;
                JLabel label = (JLabel) menuItem.getComponent(0);

                if (panelName.equals("catalog") && label.getText().equals("Book Catalog")) {
                    menuItem.setBackground(SIDEBAR_SELECTED);
                } else if (panelName.equals("borrows") && label.getText().equals("My Borrows")) {
                    menuItem.setBackground(SIDEBAR_SELECTED);
                } else if (panelName.equals("profile") && label.getText().equals("My Profile")) {
                    menuItem.setBackground(SIDEBAR_SELECTED);
                } else {
                    menuItem.setBackground(SIDEBAR_BG);
                }
            }
        }
    }

    // Set the current user and update UI
    public void setCurrentUser(User user) {
        this.currentUser = user;

    }




    private void updateSidebar() {
        // Find and update user components in sidebar
        for (Component comp : sidebarPanel.getComponents()) {
            if (comp instanceof JPanel && comp.getName() != null && comp.getName().equals("userPanel")) {
                JPanel userPanel = (JPanel) comp;

                // Update user info
                for (Component c : userPanel.getComponents()) {
                    if (c instanceof JPanel && c.getName() != null) {
                        if (c.getName().equals("userInfoPanel")) {
                            JPanel infoPanel = (JPanel) c;

                            // Update name and email labels
                            for (Component label : infoPanel.getComponents()) {
                                if (label instanceof JLabel) {
                                    JLabel jLabel = (JLabel) label;
                                    if (jLabel.getName() != null) {
                                        if (jLabel.getName().equals("nameLabel")) {
                                            jLabel.setText(currentUser.getFullName());
                                        } else if (jLabel.getName().equals("emailLabel")) {
                                            jLabel.setText(currentUser.getEmail());
                                        }
                                    }
                                }
                            }
                        } else if (c.getName().equals("avatarPanel")) {
                            // Update avatar
                            JPanel avatarPanel = (JPanel) c;
                            for (Component avatar : avatarPanel.getComponents()) {
                                if (avatar instanceof JLabel) {
                                    JLabel avatarLabel = (JLabel) avatar;
                                    avatarLabel.setText(getInitials(currentUser.getFirstname(), currentUser.getLastname()));
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
    }
}
