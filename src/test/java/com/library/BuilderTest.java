package com.library;

import com.library.creational.builder.TransactionBuilder;
import com.library.model.Transaction;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BuilderTest {

    @Test
    public void testTransactionBuilderWithAllFields() {
        // Arrange
        String expectedDueDateStr = "2026-08-07T00:00:00";
        LocalDateTime expectedDueDate = LocalDateTime.parse(expectedDueDateStr);

        // Act
        Transaction transaction = new TransactionBuilder()
            .setTransactionId("TR-001")
            .setMemberId("MEM-001")
            .setBookId("BK-001")
            .setType("BORROW")
            .setDueDate("2026-08-07T00:00:00")  // ISO format with time
            .setPriority(true)
            .setInsurance(true)
            .build();

        // Assert
        assertNotNull(transaction);
        assertEquals("TR-001", transaction.getTransactionId());
        assertEquals("MEM-001", transaction.getMemberId());
        assertEquals("BK-001", transaction.getBookId());
        assertEquals("BORROW", transaction.getType());
        assertEquals(expectedDueDate, transaction.getDueDate()); // compare LocalDateTime
        assertTrue(transaction.isPriority());
        assertTrue(transaction.isInsurance());
        assertTrue(transaction.isActive());
    }

    @Test
    public void testTransactionBuilderWithoutOptionalFields() {
        // Act
        Transaction transaction = new TransactionBuilder()
            .setTransactionId("TR-002")
            .setMemberId("MEM-002")
            .setBookId("BK-002")
            .setType("RESERVE")
            .build();

        // Assert
        assertNotNull(transaction);
        assertEquals("TR-002", transaction.getTransactionId());
        assertEquals("MEM-002", transaction.getMemberId());
        assertEquals("BK-002", transaction.getBookId());
        assertEquals("RESERVE", transaction.getType());
        assertFalse(transaction.isPriority());
        assertFalse(transaction.isInsurance());
        assertNotNull(transaction.getDueDate()); // Auto-generated, should not be null
        // Check that due date is in the future (since default is 1 hour from now)
        assertTrue(transaction.getDueDate().isAfter(LocalDateTime.now().minusMinutes(5)));
    }

    @Test
    public void testTransactionBuilderWithDefaultDueDate() {
        // Act
        Transaction transaction = new TransactionBuilder()
            .setTransactionId("TR-003")
            .setMemberId("MEM-003")
            .setBookId("BK-003")
            .setType("BORROW")
            .build();

        // Assert
        assertNotNull(transaction.getDueDate());
        // Default due date should be 1 hour from now (since DEFAULT_LOAN_HOURS = 1)
        LocalDateTime now = LocalDateTime.now();
        assertTrue(transaction.getDueDate().isAfter(now.minusMinutes(5)));
        assertTrue(transaction.getDueDate().isBefore(now.plusMinutes(70))); // within 1 hour 10 min
    }
}