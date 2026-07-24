package com.library.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private String memberId;
    private String bookId;
    private String type; // BORROW, RETURN, RESERVE
    private String dueDate;
    private String actualReturnDate;
    private double fineAmount;
    private boolean isActive;
    private boolean priority;
    private boolean insurance;

    public Transaction(String transactionId, String memberId, String bookId, String type) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.type = type;
        this.fineAmount = 0.0;
        this.isActive = true;
        this.priority = false;
        this.insurance = false;
        
        // Set due date to 14 days from now
        this.dueDate = LocalDate.now().plusDays(14)
                .format(DateTimeFormatter.ISO_DATE);
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(String actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isPriority() { return priority; }
    public void setPriority(boolean priority) { this.priority = priority; }

    public boolean isInsurance() { return insurance; }
    public void setInsurance(boolean insurance) { this.insurance = insurance; }

    public long getOverdueDays() {
        LocalDate due = LocalDate.parse(dueDate);
        LocalDate today = LocalDate.now();
        if (today.isAfter(due)) {
            return java.time.temporal.ChronoUnit.DAYS.between(due, today);
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Transaction[ID=%s, Member=%s, Book=%s, Type=%s, Due=%s, Active=%s]",
                transactionId, memberId, bookId, type, dueDate, isActive);
    }
}