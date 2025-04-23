package model.user;

import model.Book;

public class AdminBorrowStrategy implements BorrowStrategy {
    @Override
    public boolean borrow(Book book) {
        if (book.getStockBooks() > 0) {
            book.setAvailableBooks(book.getAvailableBooks() - 1);
            return true;
        }
        return false;
    }
}
