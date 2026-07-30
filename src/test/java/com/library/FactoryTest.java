package com.library;

import com.library.creational.factory.BookFactory;
import com.library.creational.factory.PhysicalBookFactory;
import com.library.creational.factory.EBookFactory;
import com.library.model.Book;
import com.library.model.PhysicalBook;
import com.library.model.EBook;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FactoryTest {

    @Test
    public void testPhysicalBookFactory() {
        System.out.println("\n=========================================");
        System.out.println("TEST: Physical Book Factory");
        System.out.println("=========================================");
        
        // Create factory and book
        BookFactory factory = new PhysicalBookFactory();
        Book book = factory.createBook("BK-001", "Design Patterns", "GoF", "978-0201633610");
        
        // Assertions
        assertNotNull(book, "Book should not be null");
        assertTrue(book instanceof PhysicalBook, "Book should be a PhysicalBook instance");
        assertEquals("Design Patterns", book.getTitle(), "Title should match");
        assertEquals("GoF", book.getAuthor(), "Author should match");
        assertEquals("Physical", book.getBookType(), "Book type should be Physical");
        assertEquals(3, book.getCopiesAvailable(), "Should have 3 copies");
        
        System.out.println("✅ Physical Book Factory Test Passed!");
        System.out.println("   Book: " + book);
    }

    @Test
    public void testEBookFactory() {
        System.out.println("\n=========================================");
        System.out.println("TEST: E-Book Factory");
        System.out.println("=========================================");
        
        // Create factory and book
        BookFactory factory = new EBookFactory();
        Book book = factory.createBook("BK-002", "Clean Code", "Robert Martin", "978-0132350884");
        
        // Assertions
        assertNotNull(book, "Book should not be null");
        assertTrue(book instanceof EBook, "Book should be an EBook instance");
        assertEquals("Clean Code", book.getTitle(), "Title should match");
        assertEquals("Robert Martin", book.getAuthor(), "Author should match");
        assertEquals("E-Book", book.getBookType(), "Book type should be E-Book");
        assertEquals(Integer.MAX_VALUE, book.getCopiesAvailable(), "E-Book should have unlimited copies");
        
        System.out.println("✅ E-Book Factory Test Passed!");
        System.out.println("   Book: " + book);
    }

    @Test
    public void testBookFactoryCreatesDifferentTypes() {
        System.out.println("\n=========================================");
        System.out.println("TEST: Factory Creates Different Types");
        System.out.println("=========================================");
        
        // Create both factories
        BookFactory physicalFactory = new PhysicalBookFactory();
        BookFactory eBookFactory = new EBookFactory();
        
        // Create books
        Book physicalBook = physicalFactory.createBook("BK-003", "Java Programming", "Author1", "ISBN-001");
        Book eBook = eBookFactory.createBook("BK-004", "Python Programming", "Author2", "ISBN-002");
        
        // Assert different types
        assertNotEquals(physicalBook.getClass(), eBook.getClass(), "Should be different classes");
        assertNotEquals(physicalBook.getBookType(), eBook.getBookType(), "Should have different types");
        assertTrue(physicalBook instanceof PhysicalBook, "Should be PhysicalBook");
        assertTrue(eBook instanceof EBook, "Should be EBook");
        
        System.out.println("✅ Different Types Test Passed!");
        System.out.println("   Physical Book: " + physicalBook.getBookType());
        System.out.println("   E-Book: " + eBook.getBookType());
    }

    @Test
    public void testBookFactoryCreatesValidBooks() {
        System.out.println("\n=========================================");
        System.out.println("TEST: Factory Creates Valid Books");
        System.out.println("=========================================");
        
        BookFactory factory = new PhysicalBookFactory();
        Book book = factory.createBook("BK-005", "Test Book", "Test Author", "ISBN-003");
        
        // Check all fields
        assertEquals("BK-005", book.getBookId());
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("ISBN-003", book.getIsbn());
        assertTrue(book.isAvailable(), "Book should be available");
        assertNotNull(book.getLocation(), "Location should not be null");
        
        System.out.println("✅ Valid Book Test Passed!");
        System.out.println("   Book: " + book);
    }
}