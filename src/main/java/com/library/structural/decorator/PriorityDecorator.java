package com.library.structural.decorator;

import com.library.model.Book;

public class PriorityDecorator extends BookDecorator {
    private static final double PRIORITY_COST = 100.0;
    
    public PriorityDecorator(Book book) {
        super(book);
    }
    
    @Override
    public String getDescription() {
        return decoratedBook.getTitle() + " + Priority";
    }
    
    @Override
    public double getCost() {
        return decoratedBook.getBaseCost() + PRIORITY_COST;
    }
    
    @Override
    public String toString() {
        return "Book[" + getDescription() + ", Cost=Rs." + getCost() + "]";
    }
}