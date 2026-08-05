package com.library;

import com.library.model.Book;
import com.library.model.PhysicalBook;
import com.library.model.Transaction;
import com.library.model.User;
import com.library.structural.facade.LibraryFacade;
import com.library.util.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class FacadeTest {

    private LibraryFacade facade;
    private User testUser;
    private Book testBook;

    @BeforeEach
    public void setUp() {
        // Clear test data before each test
        clearTestData();

        facade = new LibraryFacade();

        // Setup test data with unique IDs
        testUser = new User("MEM-TEST-001", "Test User", "test@email.com", "STUDENT");
        testBook = new PhysicalBook("BK-TEST-001", "Test Book", "Test Author",
                "TEST-ISBN-001", 3, "A-10", "NEW");

        facade.addUser(testUser);
        facade.addBook(testBook);
    }

    private void clearTestData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            // Delete in reverse order of dependencies
            stmt.executeUpdate("DELETE FROM transactions WHERE member_id LIKE 'MEM-TEST-%'");
            stmt.executeUpdate("DELETE FROM fine_transactions WHERE user_id LIKE 'MEM-TEST-%'");
            stmt.executeUpdate("DELETE FROM users WHERE user_id LIKE 'MEM-TEST-%'");
            stmt.executeUpdate("DELETE FROM books WHERE book_id LIKE 'BK-TEST-%'");
            System.out.println("🧹 Test data cleared from database.");
        } catch (SQLException e) {
            System.err.println("⚠️ Could not clear test data: " + e.getMessage());
        }
    }

    @Test
    public void testBorrowBookProcess() {
        // Borrow a book
        boolean success = facade.borrowBook("MEM-TEST-001", "BK-TEST-001");
        assertTrue(success);

        // Check if book status updated
        Book book = facade.getBook("BK-TEST-001");
        assertEquals(2, book.getCopiesAvailable());

        // Check if transaction created
        assertFalse(facade.getUserTransactions("MEM-TEST-001").isEmpty());
        Transaction transaction = facade.getUserTransactions("MEM-TEST-001").get(0);
        assertEquals("BK-TEST-001", transaction.getBookId());
        assertTrue(transaction.isActive());
    }

    @Test
    public void testBorrowUnavailableBook() {
        // First borrow
        facade.borrowBook("MEM-TEST-001", "BK-TEST-001");

        // Try to borrow same book again
        boolean success = facade.borrowBook("MEM-TEST-001", "BK-TEST-001");

        // Should fail because book is now borrowed
        assertFalse(success);
        assertEquals(2, facade.getBook("BK-TEST-001").getCopiesAvailable());
    }

    @Test
    public void testReturnBook() {
        // Borrow first
        facade.borrowBook("MEM-TEST-001", "BK-TEST-001");
        assertEquals(2, facade.getBook("BK-TEST-001").getCopiesAvailable());
        assertEquals(1, facade.getUser("MEM-TEST-001").getBooksBorrowed());

        // Return
        boolean success = facade.returnBook("MEM-TEST-001", "BK-TEST-001");

        assertTrue(success);
        assertEquals(3, facade.getBook("BK-TEST-001").getCopiesAvailable());
        assertEquals(0, facade.getUser("MEM-TEST-001").getBooksBorrowed());

        // Transaction should be inactive
        Transaction transaction = facade.getUserTransactions("MEM-TEST-001").get(0);
        assertFalse(transaction.isActive());
    }

    @Test
    public void testReturnNonExistentBook() {
        // Try to return a book that wasn't borrowed
        boolean success = facade.returnBook("MEM-TEST-001", "BK-999");
        assertFalse(success);
    }

    @Test
    public void testGetAllBooks() {
        // Add more books
        Book book2 = new PhysicalBook("BK-TEST-002", "Second Book", "Author2",
                "ISBN-002", 2, "B-15", "NEW");
        Book book3 = new PhysicalBook("BK-TEST-003", "Third Book", "Author3",
                "ISBN-003", 1, "C-20", "NEW");
        facade.addBook(book2);
        facade.addBook(book3);

        assertEquals(3, facade.getAllBooks().size());
    }

    @Test
    public void testGetAllUsers() {
        User user2 = new User("MEM-TEST-002", "Test User 2", "test2@email.com", "FACULTY");
        facade.addUser(user2);

        assertEquals(2, facade.getAllUsers().size());
    }

    @Test
    public void testUserExists() {
        assertTrue(facade.userExists("MEM-TEST-001"));
        assertFalse(facade.userExists("MEM-TEST-999"));
    }
}