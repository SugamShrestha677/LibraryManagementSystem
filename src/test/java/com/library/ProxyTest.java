package com.library;

import com.library.structural.proxy.SecurityProxy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProxyTest {

    @Test
    public void testAdminAccess() {
        // Admin should have access
        SecurityProxy proxy = new SecurityProxy("admin", "admin123");

        // Admin operations should work
        assertDoesNotThrow(() -> proxy.viewAllBooks());
        assertDoesNotThrow(() -> proxy.viewAllUsers());
        assertDoesNotThrow(() -> proxy.deleteBook("BK-001"));
    }

    @Test
    public void testMemberAccessRestriction() {
        // Member should have limited access
        SecurityProxy proxy = new SecurityProxy("member", "member123");

        // Member can view books
        assertDoesNotThrow(() -> proxy.viewAllBooks());

        // Member cannot delete books or view users
        assertThrows(SecurityException.class, () -> proxy.deleteBook("BK-001"));
        assertThrows(SecurityException.class, () -> proxy.viewAllUsers());
    }

    @Test
    public void testInvalidCredentials() {
        // Invalid credentials should throw exception
        assertThrows(SecurityException.class, () -> {
            new SecurityProxy("invalid", "wrongpass");
        });
    }

    @Test
    public void testLibrarianAccess() {
        // Librarian should have limited admin access
        SecurityProxy proxy = new SecurityProxy("librarian", "lib123");

        assertDoesNotThrow(() -> proxy.generateReport("Book"));
        assertDoesNotThrow(() -> proxy.viewAllBooks());

        // Librarian cannot delete books or view users
        assertThrows(SecurityException.class, () -> proxy.deleteBook("BK-001"));
        assertThrows(SecurityException.class, () -> proxy.viewAllUsers());
    }

    @Test
    public void testReportGeneration() {
        SecurityProxy proxy = new SecurityProxy("librarian", "lib123");

        assertDoesNotThrow(() -> proxy.generateReport("Book"));
        assertDoesNotThrow(() -> proxy.generateReport("Member"));
        assertDoesNotThrow(() -> proxy.generateReport("Transaction"));
    }

    @Test
    public void testUnauthorizedReportGeneration() {
        SecurityProxy proxy = new SecurityProxy("member", "member123");

        // Member cannot generate reports
        assertThrows(SecurityException.class, () -> proxy.generateReport("Book"));
    }
}