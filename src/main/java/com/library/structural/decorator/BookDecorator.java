package com.library.structural.decorator;

import com.library.model.Book;

public abstract class BookDecorator extends Book {
    protected Book decoratedBook;
    
    public BookDecorator(Book book) {
        super(book.getBookId(), book.getTitle(), book.getAuthor(), 
              book.getIsbn(), book.getCopiesAvailable());
        this.decoratedBook = book;
        this.baseCost = book.getBaseCost();
    }
    
    @Override
    public String getBookType() {
        return decoratedBook.getBookType() + " (Decorated)";
    }
    
    @Override
    public String getLocation() {
        return decoratedBook.getLocation();
    }
    
    @Override
    public abstract String getDescription();
    
    @Override
    public abstract double getCost();
}