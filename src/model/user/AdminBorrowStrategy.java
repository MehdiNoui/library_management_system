package model.user;

import model.Book;

public class AdminBorrowStrategy implements BorrowStrategy {
    @Override
    public boolean borrow(Book book) {
        // Special borrowing logic for admins
        // Admins can borrow books even if they're not available for regular readers
        if (book.getStockBooks() > 0) {
            book.setAvailableBooks(book.getAvailableBooks() - 1);
            return true;
        }
        return false;
    }
}
