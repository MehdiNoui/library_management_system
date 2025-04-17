package model.user;

import model.Borrow;
import model.Library;

public class ReaderBorrowStrategy implements BorrowStrategy {
    private static final int MAX_BORROW = 3;
    @Override
    public boolean canBorrow(User user) {
        int activeBorrows = 0;
        for (Borrow borrow : Library.getInstance().getBorrows()) {
            if (borrow.getIdUser().equals(user.getId())) {
                activeBorrows++;
            }
        }
        return activeBorrows < MAX_BORROW;
    }
}