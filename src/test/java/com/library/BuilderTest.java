package com.library;

import com.library.creational.builder.TransactionBuilder;
import com.library.model.Transaction;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BuilderTest {

    @Test
    public void testTransactionBuilderWithAllFields() {
        Transaction transaction = new TransactionBuilder()
            .setTransactionId("TR-001")
            .setMemberId("MEM-001")
            .setBookId("BK-001")
            .setType("BORROW")
            .setDueDate("2026-08-07")
            .setPriority(true)
            .setInsurance(true)
            .build();
        
        assertNotNull(transaction);
        assertEquals("TR-001", transaction.getTransactionId());
        assertEquals("MEM-001", transaction.getMemberId());
        assertEquals("BK-001", transaction.getBookId());
        assertEquals("BORROW", transaction.getType());
        assertEquals("2026-08-07", transaction.getDueDate());
        assertTrue(transaction.isPriority());
        assertTrue(transaction.isInsurance());
        assertTrue(transaction.isActive());
    }

    @Test
    public void testTransactionBuilderWithoutOptionalFields() {
        Transaction transaction = new TransactionBuilder()
            .setTransactionId("TR-002")
            .setMemberId("MEM-002")
            .setBookId("BK-002")
            .setType("RESERVE")
            .build();
        
        assertNotNull(transaction);
        assertEquals("TR-002", transaction.getTransactionId());
        assertEquals("MEM-002", transaction.getMemberId());
        assertEquals("BK-002", transaction.getBookId());
        assertEquals("RESERVE", transaction.getType());
        assertFalse(transaction.isPriority());
        assertFalse(transaction.isInsurance());
        assertNotNull(transaction.getDueDate()); // Auto-generated
    }

    @Test
    public void testTransactionBuilderWithDefaultDueDate() {
        Transaction transaction = new TransactionBuilder()
            .setTransactionId("TR-003")
            .setMemberId("MEM-003")
            .setBookId("BK-003")
            .setType("BORROW")
            .build();
        
        assertNotNull(transaction.getDueDate());
        assertTrue(transaction.getDueDate().length() > 0);
    }
}