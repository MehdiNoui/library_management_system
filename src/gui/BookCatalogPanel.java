package gui;

import model.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookCatalogPanel extends JPanel {
  private JTextField searchField;
  private JComboBox<String> genreFilter;
  private JPanel booksContainer;
  
  // Sample book data
  private static String[] bookIds = {"B001", "B002", "B003", "B004", "B005", "B006", "B007", "B008"};
  private static String[] bookNames = {
      "The Great Gatsby", 
      "To Kill a Mockingbird", 
      "1984", 
      "Pride and Prejudice", 
      "The Hobbit", 
      "Harry Potter and the Sorcerer's Stone",
      "The Catcher in the Rye",
      "The Lord of the Rings"
  };
  private static String[] authorNames = {
      "F. Scott Fitzgerald", 
      "Harper Lee", 
      "George Orwell", 
      "Jane Austen", 
      "J.R.R. Tolkien",
      "J.K. Rowling",
      "J.D. Salinger",
      "J.R.R. Tolkien"
  };
  private static String[] publishDates = {
      "1925-04-10", 
      "1960-07-11", 
      "1949-06-08", 
      "1813-01-28", 
      "1937-09-21",
      "1997-06-26",
      "1951-07-16",
      "1954-07-29"
  };
  private static String[] genres = {
      "Fiction", 
      "Fiction", 
      "Science Fiction", 
      "Romance", 
      "Fantasy",
      "Fantasy",
      "Fiction",
      "Fantasy"
  };
  private static String[] descriptions = {
      "The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald. Set in the Jazz Age on Long Island, the novel depicts narrator Nick Carraway's interactions with mysterious millionaire Jay Gatsby and Gatsby's obsession to reunite with his former lover, Daisy Buchanan.",
      "To Kill a Mockingbird is a novel by Harper Lee published in 1960. It was immediately successful, winning the Pulitzer Prize, and has become a classic of modern American literature.",
      "1984 is a dystopian novel by English novelist George Orwell. It was published on 8 June 1949 as Orwell's ninth and final book completed in his lifetime.",
      "Pride and Prejudice is an 1813 romantic novel of manners written by Jane Austen. The novel follows the character development of Elizabeth Bennet, the dynamic protagonist of the book.",
      "The Hobbit, or There and Back Again is a children's fantasy novel by English author J. R. R. Tolkien. It was published on 21 September 1937 to wide critical acclaim.",
      "Harry Potter and the Philosopher's Stone is a fantasy novel written by British author J. K. Rowling. The first novel in the Harry Potter series and Rowling's debut novel.",
      "The Catcher in the Rye is a novel by J. D. Salinger, partially published in serial form in 1945–1946 and as a novel in 1951.",
      "The Lord of the Rings is an epic high-fantasy novel by English author and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's 1937 children's book The Hobbit."
  };
  private static int[] stockBooks = {10, 8, 12, 6, 15, 20, 7, 9};
  private static int[] availableBooks = {8, 5, 10, 6, 12, 18, 7, 8};
  
  public BookCatalogPanel() {
      setLayout(new BorderLayout());
      setBackground(new Color(245, 245, 250));
      
      // Create header panel
      createHeaderPanel();
      
      // Create books panel
      createBooksPanel();
  }
  
  private void createHeaderPanel() {
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(Color.WHITE);
      headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      
      JLabel titleLabel = new JLabel("Book Catalog");
      titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
      
      JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      searchPanel.setBackground(Color.WHITE);
      
      // Search field
      searchField = new JTextField(20);
      searchField.setPreferredSize(new Dimension(200, 30));
      JButton searchButton = new JButton("Search");
      searchButton.setPreferredSize(new Dimension(100, 30));
      
      // Genre filter
      JLabel genreLabel = new JLabel("Genre:");
      genreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      
      genreFilter = new JComboBox<>(new String[]{"All Genres", "Fiction", "Science Fiction", "Romance", "Fantasy"});
      genreFilter.setPreferredSize(new Dimension(150, 30));
      genreFilter.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              // Filter books by genre (to be implemented)
          }
      });
      
      searchPanel.add(genreLabel);
      searchPanel.add(genreFilter);
      searchPanel.add(searchField);
      searchPanel.add(searchButton);
      
      headerPanel.add(titleLabel, BorderLayout.WEST);
      headerPanel.add(searchPanel, BorderLayout.EAST);
      
      add(headerPanel, BorderLayout.NORTH);
  }
  
  private void createBooksPanel() {
      // Create a panel with a grid layout for the books
      booksContainer = new JPanel();
      booksContainer.setLayout(new GridLayout(0, 3, 20, 20)); // 3 columns, dynamic rows
      booksContainer.setBackground(new Color(245, 245, 250));
      booksContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      
      // Create book cards
      for (int i = 0; i < bookIds.length; i++) {
          // Create a sample book
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          Date publishDate = null;
          try {
              publishDate = dateFormat.parse(publishDates[i]);
          } catch (Exception e) {
              publishDate = new Date(); // Default to current date if parsing fails
          }
          
          Book book = new Book(
              bookIds[i],
              bookNames[i],
              authorNames[i],
              publishDate,
              genres[i],
              stockBooks[i]
          );
          book.setAvailableBooks(availableBooks[i]);
          
          // Create and add book card
          BookCardPanel bookCard = new BookCardPanel(book, descriptions[i], null);
          booksContainer.add(bookCard);
      }
      
      // Create a scroll pane for the books container
      JScrollPane scrollPane = new JScrollPane(booksContainer);
      scrollPane.setBorder(BorderFactory.createEmptyBorder());
      scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling
      
      add(scrollPane, BorderLayout.CENTER);
  }
}
