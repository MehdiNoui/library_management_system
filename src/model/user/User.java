package model.user;

import model.Book;

import java.util.Date;

public abstract class User {
    protected String id;
    protected String firstname;
    protected String lastname;
    protected Date signupDate;
    protected String email;
    protected String password;
    protected BorrowStrategy borrowStrategy;

    public User(String id, String firstname, String lastname, String email, String password, Date signupDate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.signupDate = signupDate;
    }

    public void setBorrowStrategy(BorrowStrategy strategy) {
        this.borrowStrategy = strategy;
    }
    public boolean canBorrow() {
        return borrowStrategy != null && borrowStrategy.canBorrow(this);
    }
    public abstract boolean manage();
    public abstract void update(Book book);

    // Getters
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getId() {
        return id;
    }
}
