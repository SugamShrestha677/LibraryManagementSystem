package com.library.structural.facade;

import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.PhysicalBook;
import com.library.model.Transaction;
import com.library.model.User;
import com.library.creational.singleton.LibraryConfig;
import com.library.creational.builder.TransactionBuilder;
import com.library.behavioral.observer.BookStatus;
import com.library.behavioral.observer.NotificationService;
import com.library.repository.jdbc.*;
import com.library.util.DatabaseConnection;
import com.library.behavioral.strategy.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class LibraryFacade {

    // ===== REPOSITORIES (Database Access) =====
    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    private final TransactionRepository transactionRepo;
    private final FineTransactionRepository fineRepo;
    private final NotificationService notificationService;
    private int transactionCounter;

    // ===== CONSTRUCTOR =====
    public LibraryFacade() {
        this.userRepo = new UserRepository();
        this.bookRepo = new BookRepository();
        this.transactionRepo = new TransactionRepository();
        this.fineRepo = new FineTransactionRepository();
        this.notificationService = new NotificationService();

        // Initialize transaction counter from database
        try {
            int max = transactionRepo.getMaxTransactionNumber();
            this.transactionCounter = max + 1;
            System.out.println("🔢 Transaction counter initialized from DB: " + this.transactionCounter);
        } catch (SQLException e) {
            this.transactionCounter = 1;
            System.err.println("⚠️ Could not read max transaction ID, starting from 1.");
        }
    }
    // =============================================
    // ===== BOOK MANAGEMENT =====
    // =============================================

    public void addBook(Book book) {
        try {
            bookRepo.save(book);
            System.out.println("    [Facade] Book added: " + book.getTitle());
        } catch (SQLException e) {
            System.err.println("    [Facade] Failed to add book: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean deleteBook(String bookId) {
        try {
            // First check if book exists
            Optional<Book> bookOpt = bookRepo.findById(bookId);
            if (bookOpt.isEmpty()) {
                System.out.println("    [Facade] Book not found: " + bookId);
                return false;
            }
            bookRepo.delete(bookId);
            System.out.println("    [Facade] Book deleted: " + bookOpt.get().getTitle());
            return true;
        } catch (SQLException e) {
            System.err.println("    [Facade] Failed to delete book: " + e.getMessage());
            return false;
        }
    }

    public Book getBook(String bookId) {
        try {
            return bookRepo.findById(bookId).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Book> getAllBooks() {
        try {
            return bookRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void displayAllBooks() {
        List<Book> books = getAllBooks();
        System.out.println("\n=========================================");
        System.out.println("📚 LIBRARY CATALOG");
        System.out.println("=========================================");
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            // Header
            System.out.printf("%-10s | %-30s | %-20s | %-12s | %-8s | %-25s%n",
                    "Book ID", "Title", "Author", "Type", "Copies", "Details");
            System.out.println("-".repeat(105));

            for (Book book : books) {
                String details = "";
                if (book instanceof PhysicalBook) {
                    PhysicalBook pb = (PhysicalBook) book;
                    details = "Shelf: " + pb.getShelfLocation() + ", " + pb.getCondition();
                } else if (book instanceof EBook) {
                    EBook eb = (EBook) book;
                    details = "Format: " + eb.getFileFormat() + ", " + eb.getFileSize() + "MB";
                }
                System.out.printf("%-10s | %-30s | %-20s | %-12s | %-8d | %-25s%n",
                        book.getBookId(),
                        truncate(book.getTitle(), 30),
                        truncate(book.getAuthor(), 20),
                        book.getBookType(),
                        book.getCopiesAvailable(),
                        truncate(details, 25));
            }
        }
        System.out.println("Total Books: " + books.size());
        System.out.println("=========================================");
    }

    // Helper method to truncate long text
    private String truncate(String str, int maxLen) {
        if (str == null) return "";
        if (str.length() <= maxLen) return str;
        return str.substring(0, maxLen - 3) + "...";
    }
    public int getBookCount() {
        try {
            return bookRepo.findAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isBookAvailable(String bookId) {
        try {
            Optional<Book> bookOpt = bookRepo.findById(bookId);
            return bookOpt.isPresent() && bookOpt.get().isAvailable();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =============================================
    // ===== USER MANAGEMENT =====
    // =============================================

    public boolean addUser(User user) {
        try {
            userRepo.save(user);
            System.out.println("    [Facade] User registered: " + user.getName());
            return true;
        } catch (SQLException e) {
            // Provide a user-friendly message based on the error
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                System.err.println("    [ERROR] Email already exists. Please use a different email.");
            } else {
                System.err.println("    [ERROR] Failed to register user: " + e.getMessage());
            }
            return false;
        }
    }
    
    public User getUser(String userId) {
        try {
            return userRepo.findById(userId).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void displayAllUsers() {
        List<User> users = getAllUsers();
        System.out.println("\n=========================================");
        System.out.println("👥 LIBRARY MEMBERS");
        System.out.println("=========================================");
        if (users.isEmpty()) {
            System.out.println("No users registered.");
        } else {
            // Header
            System.out.printf("%-10s | %-25s | %-25s | %-10s | %-8s | %-12s%n",
                    "User ID", "Name", "Email", "Type", "Books", "Fine (Rs.)");
            System.out.println("-".repeat(100));

            for (User user : users) {
                System.out.printf("%-10s | %-25s | %-25s | %-10s | %-8d | %-12.2f%n",
                        user.getUserId(),
                        truncate(user.getName(), 25),
                        truncate(user.getEmail(), 25),
                        user.getUserType(),
                        user.getBooksBorrowed(),
                        user.getOutstandingFine());
            }
        }
        System.out.println("Total Members: " + users.size());
        System.out.println("=========================================");
    }

    public boolean userExists(String userId) {
        try {
            return userRepo.findById(userId).isPresent();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUserCount() {
        try {
            return userRepo.findAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean canUserBorrow(String userId) {
        try {
            Optional<User> userOpt = userRepo.findById(userId);
            if (userOpt.isEmpty()) return false;
            LibraryConfig config = LibraryConfig.getInstance();
            return userOpt.get().getBooksBorrowed() < config.getMaxBooksPerMember();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =============================================
    // ===== TRANSACTION MANAGEMENT =====
    // =============================================

    public Transaction getTransaction(String transactionId) {
        try {
            return transactionRepo.findById(transactionId).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Transaction> getUserTransactions(String userId) {
        try {
            return transactionRepo.findByMemberId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Transaction> getAllTransactions() {
        try {
            return transactionRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Transaction> getActiveTransactions() {
        List<Transaction> active = new ArrayList<>();
        try {
            for (Transaction t : transactionRepo.findAll()) {
                if (t.isActive()) {
                    active.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return active;
    }

    public int getTransactionCount() {
        try {
            return transactionRepo.findAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // =============================================
    // ===== CORE BUSINESS OPERATIONS =====
    // =============================================

    public boolean borrowBook(String userId, String bookId) {
        System.out.println("\n=========================================");
        System.out.println("📖 FACADE PATTERN - BORROW BOOK PROCESS");
        System.out.println("=========================================");

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Check if user exists
            System.out.println("Step 1: Checking User...");
            Optional<User> userOpt = userRepo.findById(userId);
            if (userOpt.isEmpty()) {
                System.out.println("    [ERROR] User not found!");
                return false;
            }
            User user = userOpt.get();
            System.out.println("    [OK] User found: " + user.getName());

            // Step 2: Check if book exists and is available
            System.out.println("Step 2: Checking Book Availability...");
            Optional<Book> bookOpt = bookRepo.findById(bookId);
            if (bookOpt.isEmpty()) {
                System.out.println("    [ERROR] Book not found!");
                return false;
            }
            Book book = bookOpt.get();
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
            transactionRepo.save(transaction);
            System.out.println("    [OK] Transaction created: " + transactionId);
            System.out.println("    Due Date: " + transaction.getDueDate());

            // Step 5: Update book status
            System.out.println("Step 5: Updating Book Status...");
            book.borrowBook();
            bookRepo.update(book);
            System.out.println("    [OK] Book status updated");
            System.out.println("    Remaining copies: " + book.getCopiesAvailable());

            // Step 6: Update user record
            System.out.println("Step 6: Updating User Record...");
            user.incrementBooksBorrowed();
            userRepo.update(user);
            System.out.println("    [OK] User record updated");
            System.out.println("    Books borrowed: " + user.getBooksBorrowed());

            // Step 7: Send notification (Observer pattern)
            System.out.println("Step 7: Sending Notification (Observer Pattern)...");
            BookStatus bookStatus = new BookStatus(bookId, book.getTitle());
            bookStatus.attach(notificationService);
            bookStatus.setStatus("BORROWED");

            conn.commit(); // Commit all changes

            System.out.println("=========================================");
            System.out.println("✅ BORROW SUCCESSFUL!");
            System.out.println("Transaction ID: " + transactionId);
            System.out.println("Book: " + book.getTitle());
            System.out.println("User: " + user.getName());
            System.out.println("Due Date: " + transaction.getDueDate());
            System.out.println("=========================================");

            return true;

        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean returnBook(String userId, String bookId) {
        System.out.println("\n=========================================");
        System.out.println("📤 FACADE PATTERN - RETURN BOOK PROCESS");
        System.out.println("=========================================");

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Find active transaction
            List<Transaction> userTransactions = transactionRepo.findByMemberId(userId);
            Transaction activeTransaction = null;
            for (Transaction t : userTransactions) {
                if (t.getBookId().equals(bookId) && t.isActive()) {
                    activeTransaction = t;
                    break;
                }
            }

            if (activeTransaction == null) {
                System.out.println("    [ERROR] No active borrowing found!");
                return false;
            }

            Optional<User> userOpt = userRepo.findById(userId);
            Optional<Book> bookOpt = bookRepo.findById(bookId);

            if (userOpt.isEmpty() || bookOpt.isEmpty()) {
                System.out.println("    [ERROR] User or Book not found!");
                return false;
            }

            User user = userOpt.get();
            Book book = bookOpt.get();

            // Calculate overdue days
            long overdueDays = activeTransaction.getOverdueDays();
            System.out.println("Step 1: Calculating Overdue...");
            System.out.println("    Due Date: " + activeTransaction.getDueDate());
            System.out.println("    Days Overdue: " + overdueDays);

            double fineAmount = 0.0;
            if (overdueDays > 0) {
                System.out.println("    [WARNING] Book is overdue! Fine will be applied.");
                // Strategy pattern for fine calculation
                FineCalculator fineCalculator = new FineCalculator(user);
                fineAmount = fineCalculator.calculateFine(overdueDays);
                System.out.println("    [STRATEGY] Fine calculated: Rs." + fineAmount);
            } else {
                System.out.println("    [OK] Book returned on time.");
            }

            // Update book
            System.out.println("Step 2: Updating Book...");
            book.returnBook();
            bookRepo.update(book);
            System.out.println("    [OK] Book returned");
            System.out.println("    Available copies: " + book.getCopiesAvailable());

            // Update user
            System.out.println("Step 3: Updating User Record...");
            user.decrementBooksBorrowed();
            if (fineAmount > 0) {
                user.addFine(fineAmount);
            }
            userRepo.update(user);
            System.out.println("    [OK] User record updated");
            System.out.println("    Books borrowed: " + user.getBooksBorrowed());
            System.out.println("    Outstanding fine: Rs." + user.getOutstandingFine());

            // Update transaction
            System.out.println("Step 4: Updating Transaction...");
            activeTransaction.setActive(false);
            activeTransaction.setActualReturnDate(java.time.LocalDate.now().toString());
            activeTransaction.setFineAmount(fineAmount);
            transactionRepo.update(activeTransaction);
            System.out.println("    [OK] Transaction closed");

            // Send notification (Observer pattern)
            System.out.println("Step 5: Sending Notification (Observer Pattern)...");
            BookStatus bookStatus = new BookStatus(bookId, book.getTitle());
            bookStatus.attach(notificationService);
            bookStatus.setStatus("AVAILABLE");

            conn.commit();

            System.out.println("=========================================");
            System.out.println("✅ RETURN SUCCESSFUL!");
            System.out.println("Book: " + book.getTitle());
            System.out.println("User: " + user.getName());
            System.out.println("Fine Applied: Rs." + fineAmount);
            System.out.println("=========================================");

            return true;

        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean reserveBook(String userId, String bookId) {
        System.out.println("\n=========================================");
        System.out.println("🔒 FACADE PATTERN - RESERVE BOOK PROCESS");
        System.out.println("=========================================");

        try {
            // Check if user exists
            Optional<User> userOpt = userRepo.findById(userId);
            if (userOpt.isEmpty()) {
                System.out.println("    [ERROR] User not found!");
                return false;
            }
            User user = userOpt.get();

            // Check if book exists
            Optional<Book> bookOpt = bookRepo.findById(bookId);
            if (bookOpt.isEmpty()) {
                System.out.println("    [ERROR] Book not found!");
                return false;
            }
            Book book = bookOpt.get();

            // Create reservation transaction
            String transactionId = "TR-" + String.format("%03d", transactionCounter++);
            Transaction transaction = new TransactionBuilder()
                    .setTransactionId(transactionId)
                    .setMemberId(userId)
                    .setBookId(bookId)
                    .setType("RESERVE")
                    .build();
            transactionRepo.save(transaction);

            System.out.println("    [OK] Book reserved: " + book.getTitle());
            System.out.println("    User: " + user.getName());
            System.out.println("    Reservation ID: " + transactionId);
            System.out.println("=========================================");

            return true;

        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // =============================================
    // ===== REPORTING =====
    // =============================================

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
        try {
            List<Book> books = bookRepo.findAll();
            System.out.println("📚 BOOK REPORT");
            System.out.println("-".repeat(40));
            int totalBooks = books.size();
            int availableBooks = 0;
            int borrowedBooks = 0;

            for (Book book : books) {
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

            for (Book book : books) {
                System.out.println("  " + book.getBookId() + " | " + book.getTitle() +
                        " | " + book.getAuthor() +
                        " | Copies: " + book.getCopiesAvailable());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateMemberReport() {
        try {
            List<User> users = userRepo.findAll();
            System.out.println("👥 MEMBER REPORT");
            System.out.println("-".repeat(40));
            int totalMembers = users.size();
            int activeBorrowers = 0;

            for (User user : users) {
                if (user.getBooksBorrowed() > 0) {
                    activeBorrowers++;
                }
            }

            System.out.println("Total Members: " + totalMembers);
            System.out.println("Active Borrowers: " + activeBorrowers);
            System.out.println("-".repeat(40));

            for (User user : users) {
                System.out.println("  " + user.getUserId() + " | " + user.getName() +
                        " | " + user.getUserType() +
                        " | Books: " + user.getBooksBorrowed() +
                        " | Fine: Rs." + user.getOutstandingFine());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateTransactionReport() {
        try {
            List<Transaction> transactions = transactionRepo.findAll();
            System.out.println("📋 TRANSACTION REPORT");
            System.out.println("-".repeat(40));
            int totalTransactions = transactions.size();
            int activeTransactions = 0;

            for (Transaction t : transactions) {
                if (t.isActive()) {
                    activeTransactions++;
                }
            }

            System.out.println("Total Transactions: " + totalTransactions);
            System.out.println("Active Transactions: " + activeTransactions);
            System.out.println("-".repeat(40));

            for (Transaction t : transactions) {
                Optional<Book> bookOpt = bookRepo.findById(t.getBookId());
                Optional<User> userOpt = userRepo.findById(t.getMemberId());
                System.out.println("  " + t.getTransactionId() + " | " +
                        (userOpt.isPresent() ? userOpt.get().getName() : "Unknown") + " | " +
                        (bookOpt.isPresent() ? bookOpt.get().getTitle() : "Unknown") + " | " +
                        t.getType() + " | " +
                        (t.isActive() ? "Active" : "Returned"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =============================================
    // ===== UTILITY METHODS =====
    // =============================================

    public void clearAllData() {
        try {
            // Delete all data from tables (in reverse order of dependencies)
            transactionRepo.findAll().forEach(t -> {
                try {
                    transactionRepo.delete(t.getTransactionId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            bookRepo.findAll().forEach(b -> {
                try {
                    bookRepo.delete(b.getBookId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            userRepo.findAll().forEach(u -> {
                try {
                    userRepo.delete(u.getUserId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            transactionCounter = 1;
            System.out.println("    [Facade] All data cleared.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}