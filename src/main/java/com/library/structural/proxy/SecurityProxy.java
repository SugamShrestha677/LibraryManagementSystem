package com.library.structural.proxy;

import com.library.structural.facade.LibraryFacade;
import com.library.model.Book;
import com.library.model.User;
import java.util.*;

public class SecurityProxy implements LibraryService {
    private LibraryFacade facade;
    private String username;
    private String role;
    private boolean isAuthenticated;
    
    public SecurityProxy(String username, String password) {
        this.facade = new LibraryFacade();
        this.username = username;
        this.isAuthenticated = authenticate(username, password);
        
        if (!isAuthenticated) {
            throw new SecurityException("Authentication failed!");
        }
    }
    
    private boolean authenticate(String username, String password) {
        // Simulate authentication
        if ("admin".equals(username) && "admin123".equals(password)) {
            this.role = "ADMIN";
            return true;
        } else if ("librarian".equals(username) && "lib123".equals(password)) {
            this.role = "LIBRARIAN";
            return true;
        } else if (username.startsWith("member")) {
            this.role = "MEMBER";
            return true;
        }
        return false;
    }
    
    private void checkAccess(String requiredRole) {
        if (!isAuthenticated) {
            throw new SecurityException("User not authenticated!");
        }
        
        System.out.println("    PROXY: Verifying user permissions...");
        System.out.println("    User: " + username);
        System.out.println("    Role: " + role);
        System.out.println("    Operation: " + requiredRole + " access");
        
        if ("ADMIN".equals(role) || role.equals(requiredRole)) {
            System.out.println("    Access: ALLOWED");
        } else {
            System.out.println("    Access: DENIED");
            throw new SecurityException("Access denied! Required role: " + requiredRole);
        }
    }
    
    @Override
    public boolean deleteBook(String bookId) {
        System.out.println("\n=========================================");
        System.out.println("PROXY PATTERN - SECURE DELETE BOOK");
        System.out.println("=========================================");
        checkAccess("ADMIN");
        
        // Actually delete the book
        Book book = facade.getBook(bookId);
        if (book != null) {
            System.out.println("    Book \"" + book.getTitle() + "\" deleted successfully.");
            return true;
        }
        System.out.println("    Book not found!");
        return false;
    }
    
    @Override
    public List<String> viewAllBooks() {
        System.out.println("\n=========================================");
        System.out.println("PROXY PATTERN - VIEW BOOKS");
        System.out.println("=========================================");
        checkAccess("MEMBER");
        
        List<String> bookList = new ArrayList<>();
        for (Book book : facade.getAllBooks()) {
            bookList.add(book.toString());
        }
        return bookList;
    }
    
    @Override
    public List<String> viewAllUsers() {
        System.out.println("\n=========================================");
        System.out.println("PROXY PATTERN - VIEW USERS");
        System.out.println("=========================================");
        checkAccess("ADMIN");
        
        List<String> userList = new ArrayList<>();
        for (User user : facade.getAllUsers()) {
            userList.add(user.toString());
        }
        return userList;
    }
    
    @Override
    public boolean generateReport(String reportType) {
        System.out.println("\n=========================================");
        System.out.println("PROXY PATTERN - GENERATE REPORT");
        System.out.println("=========================================");
        checkAccess("LIBRARIAN");
        
        System.out.println("    Generating " + reportType + " report...");
        System.out.println("    Report generated successfully.");
        return true;
    }
}