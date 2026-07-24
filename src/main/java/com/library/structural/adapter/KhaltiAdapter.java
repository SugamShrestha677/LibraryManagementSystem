package com.library.structural.adapter;

import java.util.UUID;

public class KhaltiAdapter implements PaymentGateway {
    @Override
    public PaymentResult processPayment(double amount, String userId) {
        System.out.println("    KhaltiAdapter: Processing payment...");
        System.out.println("    KhaltiAdapter: User ID: " + userId);
        System.out.println("    KhaltiAdapter: Amount: Rs. " + amount);
        
        // Simulate successful payment
        String transactionId = "KHT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        return new PaymentResult(true, transactionId, amount, "Khalti payment successful");
    }
}