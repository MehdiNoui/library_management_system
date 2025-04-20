package gui;

import model.Book;
import model.user.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class BookCardPanel extends JPanel {
    private Book book;
    private String description;
    private User currentUser;

    // Colors
    private final Color CARD_BG = Color.WHITE;
    private final Color CARD_BORDER = new Color(230, 230, 230);
    private final Color TITLE_COLOR = new Color(47, 53, 66);
    private final Color AUTHOR_COLOR = new Color(86, 101, 115);
    private final Color GENRE_BG = new Color(0, 151, 167);
    private final Color GENRE_TEXT = Color.WHITE;
    private final Color BORROW_BUTTON_BG = new Color(46, 204, 113);
    private final Color BORROW_BUTTON_TEXT = Color.WHITE;
    private final Color UNAVAILABLE_BG = new Color(231, 76, 60);

    public BookCardPanel(Book book, String description, User currentUser) {
        this.book = book;
        this.description = description;
        this.currentUser = currentUser;

        setLayout(new BorderLayout());
        setBackground(CARD_BG);
        setBorder(new CompoundBorder(
                new LineBorder(CARD_BORDER, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        createBookCard();
    }

    private void createBookCard() {
        // Book cover panel (placeholder)
        JPanel coverPanel = new JPanel();
        coverPanel.setBackground(new Color(240, 240, 240));
        coverPanel.setPreferredSize(new Dimension(0, 180));
        coverPanel.setLayout(new BorderLayout());

        // Book title as placeholder for cover
        JLabel coverLabel = new JLabel(book.getBookName());
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coverLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        coverPanel.add(coverLabel, BorderLayout.CENTER);

        // Book info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BG);
        infoPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Book title
        JLabel titleLabel = new JLabel(book.getBookName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Author
        JLabel authorLabel = new JLabel("by " + book.getAuthorName());
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        authorLabel.setForeground(AUTHOR_COLOR);
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Genre tag
        JPanel genrePanel = new JPanel();
        genrePanel.setBackground(GENRE_BG);
        genrePanel.setBorder(new EmptyBorder(3, 8, 3, 8));
        genrePanel.setMaximumSize(new Dimension(150, 25));
        genrePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel genreLabel = new JLabel(book.getGenre());
        genreLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        genreLabel.setForeground(GENRE_TEXT);
        genrePanel.add(genreLabel);

        // Description (truncated)
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText(truncateText(description, 100));
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(CARD_BG);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Availability
        JPanel availabilityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        availabilityPanel.setBackground(CARD_BG);
        availabilityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel availabilityLabel = new JLabel("Available: " + book.getAvailableBooks() + " of " + book.getStockBooks());
        availabilityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        availabilityPanel.add(availabilityLabel);

        // Borrow button
        JButton borrowButton = new JButton("Borrow");
        borrowButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        borrowButton.setForeground(BORROW_BUTTON_TEXT);

        if (book.getAvailableBooks() > 0) {
            borrowButton.setBackground(BORROW_BUTTON_BG);
            borrowButton.setEnabled(true);
        } else {
            borrowButton.setBackground(UNAVAILABLE_BG);
            borrowButton.setText("Unavailable");
            borrowButton.setEnabled(false);
        }

        borrowButton.setBorderPainted(false);
        borrowButton.setFocusPainted(false);
        borrowButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Borrow functionality to be implemented
                JOptionPane.showMessageDialog(
                        BookCardPanel.this,
                        "Book borrowed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        // Add components to info panel
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(authorLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(genrePanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(descriptionArea);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(availabilityPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(borrowButton);

        // Add panels to card
        add(coverPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
    }

    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}
