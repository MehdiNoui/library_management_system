package model.user;

import model.Book;

import java.util.Date;

public class Admin extends User {
    public Admin(String id, String firstname, String lastname, String email, String password, Date signupDate) {
        super(id, firstname, lastname, email, password, signupDate);
        this.borrowStrategy = new AdminBorrowStrategy();
    }
    @Override
    public boolean manage() {
        return true;
    }
    @Override
    public void update(Book book) {
        System.out.println("Admin " + firstname + " notified about book: " + book.getBookName());
    }
}
