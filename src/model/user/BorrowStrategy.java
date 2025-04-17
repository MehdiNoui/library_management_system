package model.user;

public interface BorrowStrategy {
    boolean canBorrow(User user);
}
