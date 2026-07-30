package com.library;

import com.library.behavioral.state.BookStateContext;
import com.library.model.Book;
import com.library.model.PhysicalBook;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    @Test
    public void testBookStateTransitions() {
        Book book = new PhysicalBook("BK-001", "Design Patterns", "GoF", 
                                     "ISBN-001", 3, "A-12", "NEW");
        BookStateContext context = new BookStateContext(book);
        
        // Initial state
        assertEquals("AVAILABLE", context.getCurrentStatus());
        assertTrue(book.isAvailable());
        
        // Borrow the book
        context.borrow();
        assertEquals("BORROWED", context.getCurrentStatus());
        assertEquals(2, book.getCopiesAvailable());
        
        // Try to borrow again (should stay borrowed)
        context.borrow();
        assertEquals("BORROWED", context.getCurrentStatus());
        
        // Return the book
        context.returnBook();
        assertEquals("AVAILABLE", context.getCurrentStatus());
        assertEquals(3, book.getCopiesAvailable());
    }

    @Test
    public void testReserveState() {
        Book book = new PhysicalBook("BK-002", "Clean Code", "Robert Martin", 
                                     "ISBN-002", 2, "B-15", "NEW");
        BookStateContext context = new BookStateContext(book);
        
        context.reserve();
        assertEquals("RESERVED", context.getCurrentStatus());
        
        // Cancel reservation
        context.cancelReservation();
        assertEquals("AVAILABLE", context.getCurrentStatus());
    }

    @Test
    public void testInvalidStateTransitions() {
        Book book = new PhysicalBook("BK-003", "Java", "Author", 
                                     "ISBN-003", 1, "C-20", "NEW");
        BookStateContext context = new BookStateContext(book);
        
        // Reserve first
        context.reserve();
        assertEquals("RESERVED", context.getCurrentStatus());
        
        // Try to return (should not change)
        context.returnBook();
        assertEquals("RESERVED", context.getCurrentStatus());
        
        // Borrow from reserved state
        context.borrow();
        assertEquals("BORROWED", context.getCurrentStatus());
    }

    @Test
    public void testBorrowedToReserved() {
        Book book = new PhysicalBook("BK-004", "Python", "Author", 
                                     "ISBN-004", 1, "D-10", "NEW");
        BookStateContext context = new BookStateContext(book);
        
        context.borrow();
        assertEquals("BORROWED", context.getCurrentStatus());
        
        // Reserve while borrowed
        context.reserve();
        assertEquals("RESERVED", context.getCurrentStatus());
        
        // Borrow reserved book
        context.borrow();
        assertEquals("BORROWED", context.getCurrentStatus());
    }
}