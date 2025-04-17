package model.user;

public class AdminBorrowStrategy implements BorrowStrategy {
    @Override
    public boolean canBorrow(User user) {
        return true;
    }
}
