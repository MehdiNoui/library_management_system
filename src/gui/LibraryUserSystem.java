package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class LibraryUserSystem {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Custom colors and styling
            customizeUI();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Library User System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set a larger initial size for the frame
            frame.setSize(1400, 900);

            // Ensure the frame is resizable
            frame.setResizable(true);

            // Center the frame on the screen
            frame.setLocationRelativeTo(null);

            // Create and add the user dashboard
            UserDashboard dashboard = new UserDashboard();
            frame.add(dashboard);

            // Set minimum size for the frame to prevent components from being too small
            frame.setMinimumSize(new Dimension(900, 700));

            frame.setVisible(true);
        });
    }

    private static void customizeUI() {
        // Set modern colors for components
        UIManager.put("Panel.background", new Color(245, 245, 250));
        UIManager.put("Button.background", new Color(100, 181, 246));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));
    }
}
