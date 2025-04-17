package model;

import java.util.Date;

public class Borrow {
    private int idBorrow;
    private String idBook;
    private String idUser;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private String status; // "active", "returned", "overdue"

    public Borrow(int idBorrow, String idBook, String idUser, Date borrowDate, Date dueDate) {
        this.idBorrow = idBorrow;
        this.idBook = idBook;
        this.idUser = idUser;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = "active";
    }

    // Getters and setters
    public int getIdBorrow() {
        return idBorrow;
    }

    public void setIdBorrow(int idBorrow) {
        this.idBorrow = idBorrow;
    }

    public String getIdBook() {
        return idBook;
    }

    public void setIdBook(String idBook) {
        this.idBook = idBook;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
        if (returnDate != null) {
            this.status = "returned";
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Update status based on current date
    public void updateStatus() {
        if (returnDate != null) {
            status = "returned";
        } else if (dueDate.before(new Date())) {
            status = "overdue";
        } else {
            status = "active";
        }
    }
}
