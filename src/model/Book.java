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

    public void restock(int stock) {
        this.stockBooks += stock;
        this.availableBooks += stock;
    }

    public boolean isAvailable() {
        return availableBooks > 0;
    }

    public void borrowOne() {
        if (isAvailable()) {
            availableBooks--;
        }
    }

    public void returnOne() {
        if (availableBooks < stockBooks) {
            availableBooks++;
        }
    }

    // Getters
    public String getId() { return id; }
    public String getBookName() { return bookName; }
    public String getAuthorName() { return authorName; }
    public Date getPublishDate() { return publishDate; }
    public String getGenre() { return genre; }
    public int getStockBooks() { return stockBooks; }
    public int getAvailableBooks() { return availableBooks; }
}
