package com.library.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private String memberId;
    private String bookId;
    private String type; // BORROW, RETURN, RESERVE
    private LocalDateTime dueDate;           // ✅ LocalDateTime
    private LocalDateTime actualReturnDate;  // ✅ LocalDateTime
    private double fineAmount;
    private boolean isActive;
    private boolean priority;
    private boolean insurance;

    // Default loan period: 1 hour (for testing)
    private static final int DEFAULT_LOAN_HOURS = 1;

    public Transaction(String transactionId, String memberId, String bookId, String type) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.type = type;
        this.fineAmount = 0.0;
        this.isActive = true;
        this.priority = false;
        this.insurance = false;
        this.dueDate = LocalDateTime.now().plusHours(DEFAULT_LOAN_HOURS);
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

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    // Convenience method for string due date (if needed)
    public void setDueDate(String dueDateStr) {
        this.dueDate = LocalDateTime.parse(dueDateStr, DateTimeFormatter.ISO_DATE_TIME);
    }

    public LocalDateTime getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDateTime actualReturnDate) { this.actualReturnDate = actualReturnDate; }
    
    public void setActualReturnDate(String returnDateStr) {
        this.actualReturnDate = LocalDateTime.parse(returnDateStr, DateTimeFormatter.ISO_DATE_TIME);
    }

    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isPriority() { return priority; }
    public void setPriority(boolean priority) { this.priority = priority; }

    public boolean isInsurance() { return insurance; }
    public void setInsurance(boolean insurance) { this.insurance = insurance; }

    // Calculate overdue hours
    public long getOverdueHours() {
        if (dueDate == null) return 0;
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(dueDate)) {
            return java.time.Duration.between(dueDate, now).toHours();
        }
        return 0;
    }

    // Legacy method (kept for compatibility with Strategy pattern)
    public long getOverdueDays() {
        return getOverdueHours() / 24;
    }

    @Override
    public String toString() {
        return String.format("Transaction[ID=%s, Member=%s, Book=%s, Type=%s, Due=%s, Active=%s]",
                transactionId, memberId, bookId, type, dueDate, isActive);
    }
}