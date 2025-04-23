package model;

import model.user.Admin;
import model.user.Reader;
import model.user.User;

import java.util.*;

public class Library {
    private String name = "MyLibrary";
    private static Library instance;

    private List<User> usersDB;
    private List<Book> booksDB;
    private List<Borrow> borrowDB;
    private List<User> observers;

    private Library() {
        usersDB = new ArrayList<>(List.of(
                new Reader("u01", "Mehdi","Noui","email@example.com","abc",new Date()),
                new Reader("u02", "Massi","Nissa","email@example.com","123",new Date()),
                new Admin("u03", "Admin","Admin","email@univ-bouira.com","admin123",new Date())
        ));

        booksDB = new ArrayList<>(List.of(
                new Book("b01", "1984", "George Orwell", new Date(), "Dystopian", 10),
                new Book("b02", "To Kill a Mockingbird", "Harper Lee", new Date(), "Classic", 8),
                new Book("b03", "The Great Gatsby", "F. Scott Fitzgerald", new Date(), "Classic", 5),
                new Book("b04", "The Great Gatsby", "F. Scott Fitzgerald", new Date(), "Classic", 5),
                new Book("b05", "The Great Gatsby", "F. Scott Fitzgerald", new Date(), "Classic", 5)
        ));
        borrowDB = new ArrayList<>();
        observers = new ArrayList<>();
    }

    // Singleton instance
    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    // Book Management
    public void addBook(Book book) {
        booksDB.add(book);
        notifyObservers(book); // Notify new book added
    }
    public void deleteBook(String bookId) {
        booksDB.removeIf(book -> book.getId().equals(bookId));
    }
    public Book searchBookByTitle(String title) {
        for (Book book : booksDB) {
            if (book.getBookName().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }
    public List<Book> getBooks() {
        return booksDB;
    }

    // User Management
    public void signUpUser(User user) {
        usersDB.add(user);
    }
    public void deleteUser(String userId) {
        usersDB.removeIf(user -> user.getId().equals(userId));
    }
    public List<User> getUsers() {
        return usersDB;
    }

    // ?
    public User authorizeUser(String email, String password) {
        for (User user : usersDB) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // Borrow Management
    public void borrowForUser(Borrow borrow) {
        borrowDB.add(borrow);
        Book book = getBookById(borrow.getIdBook());
        if (book != null) {
            book.borrowOne();
            notifyObservers(book);
        }
    }
    public void returnBook(Borrow borrow) {
        Book book = getBookById(borrow.getIdBook());
        if (book != null) {
            book.returnOne();
        }
    }
    public Date dueDateForBorrow(Borrow borrow) {
        return borrow.getDueDate();
    }
    public List<Borrow> getBorrows() {
        return borrowDB;
    }

    public Book getBookById(String idBook) {
        for (Book book : booksDB) {
            if (book.getId().equals(idBook)) {
                return book;
            }
        }
        return null;
    }
    public User getUserById(String idUser) {
        for (User user : usersDB) {
            if (user.getId().equals(idUser)) {
                return user;
            }
        }
        return null;
    }

    // Observer Pattern
    public void attach(User observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    public void detach(User observer) {
        observers.remove(observer);
    }
    public void notifyObservers(Book book) {
        for (User user : observers) {
            user.update(book);
        }
    }

    // Library information
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Statistics
    public int getTotalBooks() {
        return booksDB.size();
    }
    public int getTotalUsers() {
        return usersDB.size();
    }
    public int getActiveBorrows() {
        int count = 0;
        for (Borrow borrow : borrowDB) {
            if (borrow.getReturnDate() == null) {
                count++;
            }
        }
        return count;
    }
    public int getOverdueBorrows() {
        int count = 0;
        Date today = new Date();
        for (Borrow borrow : borrowDB) {
            if (borrow.getReturnDate() == null && borrow.getDueDate().before(today)) {
                count++;
            }
        }
        return count;
    }
}
