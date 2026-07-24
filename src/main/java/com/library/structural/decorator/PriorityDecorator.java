package com.library.structural.decorator;

import com.library.model.Book;

public class PriorityDecorator extends BookDecorator {
    private static final double PRIORITY_COST = 100.0;
    
    public PriorityDecorator(Book book) {
        super(book);
    }
    
    @Override
    public double getCost() {
        return decoratedBook.getCost() + PRIORITY_COST;
    }
    
    @Override
    public String getBookType() {
        return decoratedBook.getBookType() + " + Priority";
    }
    
    @Override
    public String toString() {
        return "Book[" + decoratedBook.getTitle() + " + Priority, Cost=Rs." + getCost() + "]";
    }
}