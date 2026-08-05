package com.library;

import com.library.structural.adapter.EsewaAdapter;
import com.library.structural.adapter.KhaltiAdapter;
import com.library.structural.adapter.PaymentGateway;
import com.library.structural.adapter.PaymentResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdapterTest {

    @Test
    public void testKhaltiAdapter() {
        PaymentGateway gateway = new KhaltiAdapter();
        PaymentResult result = gateway.processPayment(100.0, "MEM-001");

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getTransactionId());
        assertTrue(result.getTransactionId().startsWith("KHT-"));
        assertEquals(100.0, result.getAmount(), 0.01);
        assertEquals("Khalti payment successful", result.getMessage());
    }

    @Test
    public void testEsewaAdapter() {
        PaymentGateway gateway = new EsewaAdapter();
        PaymentResult result = gateway.processPayment(150.0, "MEM-002");

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getTransactionId());
        assertTrue(result.getTransactionId().startsWith("ESW-"));
        assertEquals(150.0, result.getAmount(), 0.01);
        assertEquals("eSewa payment successful", result.getMessage());
    }

    @Test
    public void testBothAdaptersImplementInterface() {
        PaymentGateway khalti = new KhaltiAdapter();
        PaymentGateway esewa = new EsewaAdapter();

        assertTrue(khalti instanceof PaymentGateway);
        assertTrue(esewa instanceof PaymentGateway);
    }

    @Test
    public void testKhaltiAdapterWithDifferentAmounts() {
        PaymentGateway gateway = new KhaltiAdapter();

        PaymentResult result1 = gateway.processPayment(50.0, "USER-001");
        PaymentResult result2 = gateway.processPayment(200.0, "USER-002");

        assertEquals(50.0, result1.getAmount(), 0.01);
        assertEquals(200.0, result2.getAmount(), 0.01);
        assertNotEquals(result1.getTransactionId(), result2.getTransactionId());
    }

    @Test
    public void testEsewaAdapterWithDifferentAmounts() {
        PaymentGateway gateway = new EsewaAdapter();

        PaymentResult result1 = gateway.processPayment(75.0, "USER-001");
        PaymentResult result2 = gateway.processPayment(300.0, "USER-002");

        assertEquals(75.0, result1.getAmount(), 0.01);
        assertEquals(300.0, result2.getAmount(), 0.01);
        assertNotEquals(result1.getTransactionId(), result2.getTransactionId());
    }
}