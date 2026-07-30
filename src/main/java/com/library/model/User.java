package com.library.model;

public class User {
    private String userId;
    private String name;
    private String email;
    private String userType; // STUDENT, FACULTY, PUBLIC
    private int booksBorrowed;
    private double outstandingFine;

    public User(String userId, String name, String email, String userType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.booksBorrowed = 0;
        this.outstandingFine = 0.0;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public int getBooksBorrowed() { return booksBorrowed; }
    public void setBooksBorrowed(int booksBorrowed) { this.booksBorrowed = booksBorrowed; }

    public double getOutstandingFine() { return outstandingFine; }
    public void setOutstandingFine(double outstandingFine) { this.outstandingFine = outstandingFine; }

    public void incrementBooksBorrowed() { this.booksBorrowed++; }
    public void decrementBooksBorrowed() { this.booksBorrowed--; }

    public void addFine(double amount) { this.outstandingFine += amount; }
    public void payFine(double amount) { this.outstandingFine = Math.max(0, this.outstandingFine - amount); }
    
    public void payAllFine() {
        if (outstandingFine > 0) {
            payFine(outstandingFine);
        }
    }

    @Override
    public String toString() {
        return String.format("User[ID=%s, Name=%s, Type=%s, Books=%d, Fine=Rs.%.2f]",
                userId, name, userType, booksBorrowed, outstandingFine);
    }
}