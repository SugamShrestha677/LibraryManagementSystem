package com.library.structural.decorator;

import com.library.model.Book;

public class InsuranceDecorator extends BookDecorator {
    private static final double INSURANCE_COST = 50.0;
    
    public InsuranceDecorator(Book book) {
        super(book);
    }
    
    @Override
    public String getDescription() {
        return decoratedBook.getTitle() + " + Insurance";
    }
    
    @Override
    public double getCost() {
        return decoratedBook.getBaseCost() + INSURANCE_COST;
    }
    
    @Override
    public String toString() {
        return "Book[" + getDescription() + ", Cost=Rs." + getCost() + "]";
    }
}