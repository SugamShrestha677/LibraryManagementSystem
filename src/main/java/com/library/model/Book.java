package com.library.model;

public abstract class Book {
    protected String bookId;
    protected String title;
    protected String author;
    protected String isbn;
    protected double baseCost;
    protected int copiesAvailable;

    public Book(String bookId, String title, String author, String isbn, int copiesAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.copiesAvailable = copiesAvailable;
        this.baseCost = 0.0;
    }

    public abstract String getBookType();
    public abstract String getLocation();

    // Getters and Setters
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getCopiesAvailable() { return copiesAvailable; }
    public void setCopiesAvailable(int copiesAvailable) { this.copiesAvailable = copiesAvailable; }

    public double getBaseCost() { return baseCost; }
    public void setBaseCost(double baseCost) { this.baseCost = baseCost; }

    public boolean isAvailable() { return copiesAvailable > 0; }

    public void borrowBook() {
        if (copiesAvailable > 0) {
            copiesAvailable--;
        } else {
            throw new IllegalStateException("No copies available for book: " + title);
        }
    }

    public void returnBook() {
        copiesAvailable++;
    }

    @Override
    public String toString() {
        return String.format("Book[ID=%s, Title=%s, Author=%s, Type=%s, Copies=%d]",
                bookId, title, author, getBookType(), copiesAvailable);
    }

	public abstract String getDescription();
}