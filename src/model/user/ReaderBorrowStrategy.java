package model.user;

import model.Book;
import model.Library;

public class ReaderBorrowStrategy implements BorrowStrategy {
    @Override
    public boolean borrow(User user, Book book) {
        Library lib = Library.getInstance();
        long activeBorrows = lib.getBorrows().stream()
                .filter(b -> b.getIdUser().equals(user.getId()) && b.getReturnDate() == null)
                .count();
        if (activeBorrows >= 3) {
            return false;
        }
        if (book.getAvailableBooks() > 0) {
            book.setAvailableBooks(book.getAvailableBooks() - 1);
            return true;
        }
        return false;
    }
}