package model;

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
        usersDB = new ArrayList<>();
        booksDB = new ArrayList<>();
        borrowDB = new ArrayList<>();
        observers = new ArrayList<>();

        // Initialize with some sample data
        initializeSampleData();
    }

    // Singleton instance
    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    private void initializeSampleData() {


    }

    // Book Management
    public void addBook(Book book) {
        booksDB.add(book);
        notifyObservers(book); // Notify new book added
    }
    public void deleteBook(Book book) {
        booksDB.remove(book);
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
    public void deleteUser(User user) {
        usersDB.remove(user);
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
