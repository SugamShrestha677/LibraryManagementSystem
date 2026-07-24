package com.library.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

public class BookStatus {
    private String bookId;
    private String bookTitle;
    private String status;
    private List<Observer> observers;
    
    public BookStatus(String bookId, String bookTitle) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.status = "AVAILABLE";
        this.observers = new ArrayList<>();
    }
    
    public void attach(Observer observer) {
        observers.add(observer);
        System.out.println("   [Observer] " + observer.getClass().getSimpleName() + " attached to book: " + bookTitle);
    }
    
    public void detach(Observer observer) {
        observers.remove(observer);
        System.out.println("   [Observer] " + observer.getClass().getSimpleName() + " detached from book: " + bookTitle);
    }
    
    public void setStatus(String newStatus) {
        String oldStatus = this.status;
        this.status = newStatus;
        
        System.out.println("\n   [Observer] Book status changed from " + oldStatus + " to " + newStatus);
        
        // Notify all observers
        notifyObservers("STATUS_CHANGE", 
            "Book '" + bookTitle + "' (ID: " + bookId + ") status changed from " + 
            oldStatus + " to " + newStatus);
    }
    
    private void notifyObservers(String eventType, String message) {
        System.out.println("\n   [Observer] Notifying " + observers.size() + " observer(s)...");
        for (Observer observer : observers) {
            observer.update(eventType, message);
        }
    }
    
    public String getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public String getStatus() { return status; }
    public List<Observer> getObservers() { return observers; }
}