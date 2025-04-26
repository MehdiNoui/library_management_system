package model.user;

import model.Book;

public interface BorrowStrategy {
    boolean borrow(User user, Book book);
}