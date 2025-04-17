package model;
import java.util.Date;

public class Borrow {
    private int idBorrow;
    private String idBook;
    private String idUser;
    private Date borrowDate;
    private Date dueDate;

    public Borrow(int idBorrow, String idBook, String idUser, Date borrowDate, Date dueDate) {
        this.idBorrow = idBorrow;
        this.idBook = idBook;
        this.idUser = idUser;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    // Getters
    public int getIdBorrow() { return idBorrow; }
    public String getIdBook() { return idBook; }
    public String getIdUser() { return idUser; }
    public Date getBorrowDate() { return borrowDate; }
    public Date getDueDate() { return dueDate; }
}
