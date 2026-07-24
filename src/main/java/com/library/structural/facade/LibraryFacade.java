package com.library.structural.facade;

import com.library.model.Book;
import com.library.model.Transaction;
import com.library.model.User;
import com.library.creational.singleton.LibraryConfig;
import com.library.creational.builder.TransactionBuilder;
import com.library.behavioral.observer.BookStatus;
import com.library.behavioral.observer.NotificationService;
import java.util.*;

public class LibraryFacade {
    private Map<String, Book> books;
    private Map<String, User> users;
    private Map<String, Transaction> transactions;
    private NotificationService notificationService;
    private int transactionCounter;
    
    public LibraryFacade() {
        this.books = new HashMap<>();
        this.users = new HashMap<>();
        this.transactions = new HashMap<>();
        this.notificationService = new NotificationService();
        this.transactionCounter = 1;
    }
    
    // ===== BOOK MANAGEMENT =====
    
    public void addBook(Book book) {
        books.put(book.getBookId(), book);
        System.out.println("    [Facade] Book added: " + book.getTitle());
    }
    
    public boolean deleteBook(String bookId) {
        Book removed = books.remove(bookId);
        if (removed != null) {
            System.out.println("    [Facade] Book deleted: " + removed.getTitle());
            return true;
        }
        System.out.println("    [Facade] Book not found: " + bookId);
        return false;
    }
    
    public Book getBook(String bookId) {
        return books.get(bookId);
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
    
    public void displayAllBooks() {
        System.out.println("\n=========================================");
        System.out.println("📚 LIBRARY CATALOG");
        System.out.println("=========================================");
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            for (Book book : books.values()) {
                System.out.println(book);
            }
        }
        System.out.println("Total Books: " + books.size());
        System.out.println("=========================================");
    }
    
    // ===== USER MANAGEMENT =====
    
    public void addUser(User user) {
        users.put(user.getUserId(), user);
        System.out.println("    [Facade] User registered: " + user.getName());
    }
    
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    public void displayAllUsers() {
        System.out.println("\n=========================================");
        System.out.println("👥 LIBRARY MEMBERS");
        System.out.println("=========================================");
        if (users.isEmpty()) {
            System.out.println("No users registered.");
        } else {
            for (User user : users.values()) {
                System.out.println(user);
            }
        }
        System.out.println("Total Members: " + users.size());
        System.out.println("=========================================");
    }
    
    public boolean userExists(String userId) {
        return users.containsKey(userId);
    }
    
    // ===== TRANSACTION MANAGEMENT =====
    
    public Transaction getTransaction(String transactionId) {
        return transactions.get(transactionId);
    }
    
    public List<Transaction> getUserTransactions(String userId) {
        List<Transaction> userTransactions = new ArrayList<>();
        for (Transaction t : transactions.values()) {
            if (t.getMemberId().equals(userId)) {
                userTransactions.add(t);
            }
        }
        return userTransactions;
    }
    
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions.values());
    }
    
    public List<Transaction> getActiveTransactions() {
        List<Transaction> activeTransactions = new ArrayList<>();
        for (Transaction t : transactions.values()) {
            if (t.isActive()) {
                activeTransactions.add(t);
            }
        }
        return activeTransactions;
    }
    
    // ===== CORE BUSINESS OPERATIONS =====
    
    public boolean borrowBook(String userId, String bookId) {
        System.out.println("\n=========================================");
        System.out.println("📖 FACADE PATTERN - BORROW BOOK PROCESS");
        System.out.println("=========================================");
        
        // Step 1: Check if user exists
        System.out.println("Step 1: Checking User...");
        if (!users.containsKey(userId)) {
            System.out.println("    [ERROR] User not found!");
            return false;
        }
        User user = users.get(userId);
        System.out.println("    [OK] User found: " + user.getName());
        
        // Step 2: Check if book exists and is available
        System.out.println("Step 2: Checking Book Availability...");
        if (!books.containsKey(bookId)) {
            System.out.println("    [ERROR] Book not found!");
            return false;
        }
        Book book = books.get(bookId);
        if (!book.isAvailable()) {
            System.out.println("    [ERROR] Book not available!");
            System.out.println("    Copies available: " + book.getCopiesAvailable());
            return false;
        }
        System.out.println("    [OK] Book available: " + book.getTitle());
        System.out.println("    Copies available: " + book.getCopiesAvailable());
        
        // Step 3: Check borrowing limits
        System.out.println("Step 3: Checking Borrowing Limits...");
        LibraryConfig config = LibraryConfig.getInstance();
        if (user.getBooksBorrowed() >= config.getMaxBooksPerMember()) {
            System.out.println("    [ERROR] Borrowing limit reached!");
            System.out.println("    Books borrowed: " + user.getBooksBorrowed());
            System.out.println("    Max allowed: " + config.getMaxBooksPerMember());
            return false;
        }
        System.out.println("    [OK] Borrowing limit: " + user.getBooksBorrowed() + "/" + config.getMaxBooksPerMember());
        
        // Step 4: Create transaction using Builder pattern
        System.out.println("Step 4: Creating Transaction (Builder Pattern)...");
        String transactionId = "TR-" + String.format("%03d", transactionCounter++);
        Transaction transaction = new TransactionBuilder()
                .setTransactionId(transactionId)
                .setMemberId(userId)
                .setBookId(bookId)
                .setType("BORROW")
                .build();
        
        transactions.put(transactionId, transaction);
        System.out.println("    [OK] Transaction created: " + transactionId);
        System.out.println("    Due Date: " + transaction.getDueDate());
        
        // Step 5: Update book status
        System.out.println("Step 5: Updating Book Status...");
        book.borrowBook();
        System.out.println("    [OK] Book status updated");
        System.out.println("    Remaining copies: " + book.getCopiesAvailable());
        
        // Step 6: Update user record
        System.out.println("Step 6: Updating User Record...");
        user.incrementBooksBorrowed();
        System.out.println("    [OK] User record updated");
        System.out.println("    Books borrowed: " + user.getBooksBorrowed());
        
        // Step 7: Send notification (Observer pattern)
        System.out.println("Step 7: Sending Notification (Observer Pattern)...");
        BookStatus bookStatus = new BookStatus(bookId, book.getTitle());
        bookStatus.attach(notificationService);
        bookStatus.setStatus("BORROWED");
        
        System.out.println("=========================================");
        System.out.println("✅ BORROW SUCCESSFUL!");
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Book: " + book.getTitle());
        System.out.println("User: " + user.getName());
        System.out.println("Due Date: " + transaction.getDueDate());
        System.out.println("=========================================");
        
        return true;
    }
    
    public boolean returnBook(String userId, String bookId) {
        System.out.println("\n=========================================");
        System.out.println("📤 FACADE PATTERN - RETURN BOOK PROCESS");
        System.out.println("=========================================");
        
        // Find active transaction
        Transaction activeTransaction = null;
        for (Transaction t : transactions.values()) {
            if (t.getMemberId().equals(userId) && 
                t.getBookId().equals(bookId) && 
                t.isActive()) {
                activeTransaction = t;
                break;
            }
        }
        
        if (activeTransaction == null) {
            System.out.println("    [ERROR] No active borrowing found!");
            return false;
        }
        
        User user = users.get(userId);
        Book book = books.get(bookId);
        
        if (user == null || book == null) {
            System.out.println("    [ERROR] User or Book not found!");
            return false;
        }
        
        // Calculate overdue days
        long overdueDays = activeTransaction.getOverdueDays();
        System.out.println("Step 1: Calculating Overdue...");
        System.out.println("    Due Date: " + activeTransaction.getDueDate());
        System.out.println("    Days Overdue: " + overdueDays);
        
        if (overdueDays > 0) {
            System.out.println("    [WARNING] Book is overdue! Fine will be applied.");
            // Strategy pattern will be used for fine calculation
        } else {
            System.out.println("    [OK] Book returned on time.");
        }
        
        // Update book
        System.out.println("Step 2: Updating Book...");
        book.returnBook();
        System.out.println("    [OK] Book returned");
        System.out.println("    Available copies: " + book.getCopiesAvailable());
        
        // Update user
        System.out.println("Step 3: Updating User Record...");
        user.decrementBooksBorrowed();
        System.out.println("    [OK] User record updated");
        System.out.println("    Books borrowed: " + user.getBooksBorrowed());
        
        // Update transaction
        System.out.println("Step 4: Updating Transaction...");
        activeTransaction.setActive(false);
        activeTransaction.setActualReturnDate(java.time.LocalDate.now().toString());
        if (overdueDays > 0) {
            double fine = overdueDays * 10.0; // Simple fine calculation
            activeTransaction.setFineAmount(fine);
            user.addFine(fine);
            System.out.println("    [OK] Fine applied: Rs." + fine);
        }
        System.out.println("    [OK] Transaction closed");
        
        // Send notification (Observer pattern)
        System.out.println("Step 5: Sending Notification (Observer Pattern)...");
        BookStatus bookStatus = new BookStatus(bookId, book.getTitle());
        bookStatus.attach(notificationService);
        bookStatus.setStatus("AVAILABLE");
        
        System.out.println("=========================================");
        System.out.println("✅ RETURN SUCCESSFUL!");
        System.out.println("Book: " + book.getTitle());
        System.out.println("User: " + user.getName());
        System.out.println("Fine: Rs." + (overdueDays > 0 ? overdueDays * 10.0 : 0));
        System.out.println("=========================================");
        
        return true;
    }
    
    public boolean reserveBook(String userId, String bookId) {
        System.out.println("\n=========================================");
        System.out.println("🔒 FACADE PATTERN - RESERVE BOOK PROCESS");
        System.out.println("=========================================");
        
        // Check if user exists
        if (!users.containsKey(userId)) {
            System.out.println("    [ERROR] User not found!");
            return false;
        }
        User user = users.get(userId);
        
        // Check if book exists
        if (!books.containsKey(bookId)) {
            System.out.println("    [ERROR] Book not found!");
            return false;
        }
        Book book = books.get(bookId);
        
        // Create reservation transaction
        String transactionId = "TR-" + String.format("%03d", transactionCounter++);
        Transaction transaction = new TransactionBuilder()
                .setTransactionId(transactionId)
                .setMemberId(userId)
                .setBookId(bookId)
                .setType("RESERVE")
                .build();
        
        transactions.put(transactionId, transaction);
        
        System.out.println("    [OK] Book reserved: " + book.getTitle());
        System.out.println("    User: " + user.getName());
        System.out.println("    Reservation ID: " + transactionId);
        System.out.println("=========================================");
        
        return true;
    }
    
    // ===== REPORTING =====
    
    public void generateReport(String reportType) {
        System.out.println("\n=========================================");
        System.out.println("📊 REPORT GENERATOR");
        System.out.println("=========================================");
        System.out.println("Report Type: " + reportType);
        System.out.println("-".repeat(40));
        
        switch (reportType.toUpperCase()) {
            case "BOOK":
                generateBookReport();
                break;
            case "MEMBER":
                generateMemberReport();
                break;
            case "TRANSACTION":
                generateTransactionReport();
                break;
            default:
                System.out.println("Unknown report type: " + reportType);
        }
        
        System.out.println("=========================================");
    }
    
    private void generateBookReport() {
        System.out.println("📚 BOOK REPORT");
        System.out.println("-".repeat(40));
        int totalBooks = books.size();
        int availableBooks = 0;
        int borrowedBooks = 0;
        
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                availableBooks++;
            } else {
                borrowedBooks++;
            }
        }
        
        System.out.println("Total Books: " + totalBooks);
        System.out.println("Available: " + availableBooks);
        System.out.println("Borrowed: " + borrowedBooks);
        System.out.println("-".repeat(40));
        
        for (Book book : books.values()) {
            System.out.println("  " + book.getBookId() + " | " + book.getTitle() + 
                             " | " + book.getAuthor() + 
                             " | Copies: " + book.getCopiesAvailable());
        }
    }
    
    private void generateMemberReport() {
        System.out.println("👥 MEMBER REPORT");
        System.out.println("-".repeat(40));
        int totalMembers = users.size();
        int activeBorrowers = 0;
        
        for (User user : users.values()) {
            if (user.getBooksBorrowed() > 0) {
                activeBorrowers++;
            }
        }
        
        System.out.println("Total Members: " + totalMembers);
        System.out.println("Active Borrowers: " + activeBorrowers);
        System.out.println("-".repeat(40));
        
        for (User user : users.values()) {
            System.out.println("  " + user.getUserId() + " | " + user.getName() + 
                             " | " + user.getUserType() + 
                             " | Books: " + user.getBooksBorrowed() +
                             " | Fine: Rs." + user.getOutstandingFine());
        }
    }
    
    private void generateTransactionReport() {
        System.out.println("📋 TRANSACTION REPORT");
        System.out.println("-".repeat(40));
        int totalTransactions = transactions.size();
        int activeTransactions = 0;
        
        for (Transaction t : transactions.values()) {
            if (t.isActive()) {
                activeTransactions++;
            }
        }
        
        System.out.println("Total Transactions: " + totalTransactions);
        System.out.println("Active Transactions: " + activeTransactions);
        System.out.println("-".repeat(40));
        
        for (Transaction t : transactions.values()) {
            Book book = books.get(t.getBookId());
            User user = users.get(t.getMemberId());
            System.out.println("  " + t.getTransactionId() + " | " + 
                             (user != null ? user.getName() : "Unknown") + " | " +
                             (book != null ? book.getTitle() : "Unknown") + " | " +
                             t.getType() + " | " + 
                             (t.isActive() ? "Active" : "Returned"));
        }
    }
    
    // ===== UTILITY METHODS =====
    
    public int getBookCount() {
        return books.size();
    }
    
    public int getUserCount() {
        return users.size();
    }
    
    public int getTransactionCount() {
        return transactions.size();
    }
    
    public void clearAllData() {
        books.clear();
        users.clear();
        transactions.clear();
        transactionCounter = 1;
        System.out.println("    [Facade] All data cleared.");
    }
    
    public boolean isBookAvailable(String bookId) {
        Book book = books.get(bookId);
        return book != null && book.isAvailable();
    }
    
    public boolean canUserBorrow(String userId) {
        User user = users.get(userId);
        if (user == null) return false;
        LibraryConfig config = LibraryConfig.getInstance();
        return user.getBooksBorrowed() < config.getMaxBooksPerMember();
    }
}