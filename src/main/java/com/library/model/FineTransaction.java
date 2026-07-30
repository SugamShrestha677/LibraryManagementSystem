package com.library.model;

import java.time.LocalDate;

public class FineTransaction {
    private String fineId;
    private String userId;
    private double amount;
    private String description;
    private LocalDate transactionDate;

    public FineTransaction(String fineId, String userId, double amount, String description) {
        this.fineId = fineId;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.transactionDate = LocalDate.now();
    }

    public FineTransaction(String fineId, String userId, double amount, String description, LocalDate transactionDate) {
        this.fineId = fineId;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public String getFineId() { return fineId; }
    public void setFineId(String fineId) { this.fineId = fineId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    @Override
    public String toString() {
        return String.format("FineTransaction[ID=%s, User=%s, Amount=%.2f, Desc=%s, Date=%s]",
                fineId, userId, amount, description, transactionDate);
    }
}