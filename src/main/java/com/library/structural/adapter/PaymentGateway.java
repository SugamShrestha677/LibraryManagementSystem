package com.library.structural.adapter;

public interface PaymentGateway {
    PaymentResult processPayment(double amount, String userId);
}