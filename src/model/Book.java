package model;

import java.util.Date;

public class Book {
    private String id;
    private String bookName;
    private String authorName;
    private Date publishDate;
    private String genre;
    private int stockBooks;
    private int availableBooks;

    public Book(String id, String bookName, String authorName, Date publishDate, String genre, int stockBooks) {
        this.id = id;
        this.bookName = bookName;
        this.authorName = authorName;
        this.publishDate = publishDate;
        this.genre = genre;
        this.stockBooks = stockBooks;
        this.availableBooks = stockBooks;
    }

    public void borrowOne() {
        if (availableBooks > 0) {
            availableBooks--;
        }
    }

    public void returnOne() {
        if (availableBooks < stockBooks) {
            availableBooks++;
        }
    }

    public void restock(int stock) {
        this.stockBooks += stock;
        this.availableBooks += stock;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getStockBooks() {
        return stockBooks;
    }

    public void setStockBooks(int stockBooks) {
        this.stockBooks = stockBooks;
    }

    public int getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(int availableBooks) {
        this.availableBooks = availableBooks;
    }

    @Override
    public String toString() {
        return bookName + " by " + authorName;
    }
}
