package model.user;

import model.Book;

public class AdminBorrowStrategy implements BorrowStrategy {
    @Override
    public boolean borrow(User user, Book book) {
        if (book.getAvailableBooks() > 0) {
            book.setAvailableBooks(book.getAvailableBooks() - 1);
            return true;
        }
        return false;
    }
}