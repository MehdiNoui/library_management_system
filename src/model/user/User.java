package model.user;

import model.Book;
import java.util.Date;

public abstract class User {
    protected String id;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected String password;
    protected Date signupDate;
    protected BorrowStrategy borrowStrategy;

    public User(String id, String firstname, String lastname, String email, String password, Date signupDate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.signupDate = signupDate;
    }

    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getEmail() {
        return email;
    }
    public String getFullName() {
        return firstname + " " + lastname;
    }
    public String getPassword() {
        return password;
    }
    public Date getSignupDate() {
        return signupDate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean borrow(Book book) {
        return borrowStrategy.borrow(book);
    }

    public abstract boolean manage();

    public abstract void update(Book book);

    @Override
    public String toString() {
        return firstname + " " + lastname + " (" + email + ")";
    }
}
