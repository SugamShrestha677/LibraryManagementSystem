package com.library.structural.adapter;

import java.util.UUID;

public class EsewaAdapter implements PaymentGateway {
    @Override
    public PaymentResult processPayment(double amount, String userId) {
        System.out.println("    EsewaAdapter: Processing payment...");
        System.out.println("    EsewaAdapter: User ID: " + userId);
        System.out.println("    EsewaAdapter: Amount: Rs. " + amount);
        
        // Simulate successful payment
        String transactionId = "ESW-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        return new PaymentResult(true, transactionId, amount, "eSewa payment successful");
    }
}