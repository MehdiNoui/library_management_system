package window;

import java.awt.*;
import javax.swing.*;

import gui.*;
import model.user.Reader;

public class MainWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Reader currentReader;
    private ReaderDashboard readerDashboard;

    public MainWindow() {
        setTitle("Library System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        LoginPanel loginPanel = new LoginPanel(this);
        AdminDashboard adminDashboard = new AdminDashboard();
        readerDashboard = new ReaderDashboard(); // Store as instance variable

        mainPanel.add(loginPanel, "login");
        mainPanel.add(adminDashboard, "adminDashboard");
        mainPanel.add(readerDashboard, "readerDashboard");
        add(mainPanel);
        setVisible(true);
    }

    public void showPanel(String name) {
        // If showing reader dashboard, update it with current reader
        if (name.equals("readerDashboard") && currentReader != null) {
            readerDashboard.setReader(currentReader);
        }

        cardLayout.show(mainPanel, name);
    }

    public void setCurrentReader(Reader reader) {
        this.currentReader = reader;
    }

    public Reader getCurrentReader() {
        return currentReader;
    }
}