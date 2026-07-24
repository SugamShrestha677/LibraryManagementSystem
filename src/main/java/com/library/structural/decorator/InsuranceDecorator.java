package com.library.structural.decorator;

import com.library.model.Book;

public class InsuranceDecorator extends BookDecorator {
    private static final double INSURANCE_COST = 50.0;
    
    public InsuranceDecorator(Book book) {
        super(book);
    }
    
    @Override
    public double getCost() {
        return decoratedBook.getCost() + INSURANCE_COST;
    }
    
    @Override
    public String getBookType() {
        return decoratedBook.getBookType() + " + Insurance";
    }
    
    @Override
    public String toString() {
        return "Book[" + decoratedBook.getTitle() + " + Insurance, Cost=Rs." + getCost() + "]";
    }
}