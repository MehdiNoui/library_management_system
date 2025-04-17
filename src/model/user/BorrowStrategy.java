package model.user;

import model.Book;

public interface BorrowStrategy {
    boolean borrow(Book book);
}
