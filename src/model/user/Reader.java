package model.user;

import model.Book;

import java.util.Date;

public class Reader extends User {
    private BorrowStrategy borrowStrategy;
    public Reader(String id, String firstname, String lastname, String email, String password, Date signupDate) {
        super(id, firstname, lastname, email, password, signupDate);
        this.borrowStrategy = new ReaderBorrowStrategy();
    }
    public BorrowStrategy getBorrowStrategy() {
        return borrowStrategy;
    }

    public void setBorrowStrategy(BorrowStrategy borrowStrategy) {
        this.borrowStrategy = borrowStrategy;
    }
    @Override
    public boolean manage() {
        return false;
    }
    @Override
    public void update(Book book) {
        System.out.println("Reader " + firstname + " notified about book: " + book.getBookName());
    }
    @Override
    public String getRole() {
        return "Reader";
    }
}
