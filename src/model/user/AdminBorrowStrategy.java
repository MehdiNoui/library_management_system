package model.user;

import model.Book;

public class AdminBorrowStrategy implements BorrowStrategy {
    @Override
    public boolean borrow(User user, Book book) {
        return false;
    }
}