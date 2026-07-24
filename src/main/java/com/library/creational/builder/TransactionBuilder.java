package com.library.creational.builder;

import com.library.model.Transaction;

public class TransactionBuilder {
    private String transactionId;
    private String memberId;
    private String bookId;
    private String type;
    private String dueDate;
    private boolean priority;
    private boolean insurance;
    
    public TransactionBuilder setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }
    
    public TransactionBuilder setMemberId(String memberId) {
        this.memberId = memberId;
        return this;
    }
    
    public TransactionBuilder setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }
    
    public TransactionBuilder setType(String type) {
        this.type = type;
        return this;
    }
    
    public TransactionBuilder setDueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }
    
    public TransactionBuilder setPriority(boolean priority) {
        this.priority = priority;
        return this;
    }
    
    public TransactionBuilder setInsurance(boolean insurance) {
        this.insurance = insurance;
        return this;
    }
    
    public Transaction build() {
        Transaction transaction = new Transaction(transactionId, memberId, bookId, type);
        if (dueDate != null) {
            transaction.setDueDate(dueDate);
        }
        transaction.setPriority(priority);
        transaction.setInsurance(insurance);
        return transaction;
    }
}