package com.library.behavioral.state;

import com.library.model.Book;

public class BookStateContext {
    private BookState currentState;
    private Book book;
    
    public BookStateContext(Book book) {
        this.book = book;
        // Start with Available state
        this.currentState = new AvailableState();
        this.currentState.enter(this);
    }
    
    public void setState(BookState state) {
        this.currentState = state;
        this.currentState.enter(this);
    }
    
    public Book getBook() {
        return book;
    }
    
    public String getCurrentStatus() {
        return currentState.getStatus();
    }
    
    public void borrow() {
        currentState.handleBorrow(this);
    }
    
    public void returnBook() {
        currentState.handleReturn(this);
    }
    
    public void reserve() {
        currentState.handleReserve(this);
    }
    
    public void cancelReservation() {
        currentState.handleCancelReservation(this);
    }
}