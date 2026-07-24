package com.library.structural.adapter;

public class PaymentResult {
    private boolean success;
    private String transactionId;
    private double amount;
    private String message;
    
    public PaymentResult(boolean success, String transactionId, double amount, String message) {
        this.success = success;
        this.transactionId = transactionId;
        this.amount = amount;
        this.message = message;
    }
    
    public boolean isSuccess() { return success; }
    public String getTransactionId() { return transactionId; }
    public double getAmount() { return amount; }
    public String getMessage() { return message; }
    
    @Override
    public String toString() {
        return String.format("Payment[Success=%s, ID=%s, Amount=Rs.%.2f, Message=%s]",
                success, transactionId, amount, message);
    }
}