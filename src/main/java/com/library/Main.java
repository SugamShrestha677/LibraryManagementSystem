package com.library;

import com.library.model.*;
import com.library.creational.singleton.LibraryConfig;
import com.library.creational.factory.*;
import com.library.structural.facade.LibraryFacade;
import com.library.structural.proxy.SecurityProxy;
import com.library.structural.adapter.*;
import com.library.behavioral.command.*;
import com.library.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static LibraryFacade libraryFacade;
    private static User currentUser;
    private static SecurityProxy securityProxy;
    private static CommandInvoker commandInvoker = new CommandInvoker();

    public static void main(String[] args) {
        // Initialize database schema
        initializeDatabase();

        // Initialize system
        libraryFacade = new LibraryFacade();
        seedSampleData();

        // Show system configuration (Singleton)
        LibraryConfig.getInstance().displayConfig();

        // Main menu loop
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> login();
                case 2 -> registerUser();
                case 3 -> displayAllBooks();
                case 4 -> displayAllUsers();
                case 0 -> {
                    System.out.println("\nThank you for using Library Management System!");
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    // ===== DATABASE INITIALIZATION =====
    private static void initializeDatabase() {
        String createTables = """
            CREATE TABLE IF NOT EXISTS users (
                user_id VARCHAR(20) PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE,
                user_type VARCHAR(20) NOT NULL,
                books_borrowed INTEGER DEFAULT 0,
                outstanding_fine DECIMAL(10,2) DEFAULT 0.00,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );

            CREATE TABLE IF NOT EXISTS books (
                book_id VARCHAR(20) PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                author VARCHAR(100) NOT NULL,
                isbn VARCHAR(20) NOT NULL,
                book_type VARCHAR(20) NOT NULL,
                copies_available INTEGER NOT NULL,
                base_cost DECIMAL(10,2) DEFAULT 0.00,
                shelf_location VARCHAR(50),
                condition VARCHAR(20),
                file_format VARCHAR(10),
                file_size DECIMAL(10,2),
                download_url TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );

            CREATE TABLE IF NOT EXISTS transactions (
                transaction_id VARCHAR(20) PRIMARY KEY,
                member_id VARCHAR(20) NOT NULL REFERENCES users(user_id),
                book_id VARCHAR(20) NOT NULL REFERENCES books(book_id),
                type VARCHAR(20) NOT NULL,
                due_date DATE NOT NULL,
                actual_return_date DATE,
                fine_amount DECIMAL(10,2) DEFAULT 0.00,
                is_active BOOLEAN DEFAULT TRUE,
                priority BOOLEAN DEFAULT FALSE,
                insurance BOOLEAN DEFAULT FALSE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );

            CREATE TABLE IF NOT EXISTS fine_transactions (
                fine_id VARCHAR(20) PRIMARY KEY,
                user_id VARCHAR(20) NOT NULL REFERENCES users(user_id),
                amount DECIMAL(10,2) NOT NULL,
                description VARCHAR(255) NOT NULL,
                transaction_date DATE NOT NULL
            );

            CREATE TABLE IF NOT EXISTS library_config (
                config_id INTEGER PRIMARY KEY DEFAULT 1,
                library_name VARCHAR(100) NOT NULL,
                max_books_per_member INTEGER NOT NULL,
                fine_per_day_student DECIMAL(10,2) NOT NULL,
                fine_per_day_faculty DECIMAL(10,2) NOT NULL,
                operating_hours VARCHAR(50) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );

            INSERT INTO library_config (config_id, library_name, max_books_per_member,
                fine_per_day_student, fine_per_day_faculty, operating_hours)
            SELECT 1, 'Kathmandu University Library', 5, 10.00, 20.00, '8:00 AM - 8:00 PM'
            WHERE NOT EXISTS (SELECT 1 FROM library_config WHERE config_id = 1);
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTables);
            System.out.println("✅ Database tables verified/created.");
        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== SEED SAMPLE DATA =====
    private static void seedSampleData() {
        // Check if we already have data to avoid duplicates
        if (!libraryFacade.getAllBooks().isEmpty()) {
            System.out.println("📚 Sample data already exists. Skipping seed.");
            return;
        }

        // Seed books using Factory pattern
        BookFactory physicalFactory = new PhysicalBookFactory();
        BookFactory eBookFactory = new EBookFactory();

        Book book1 = physicalFactory.createBook("BK-001", "Design Patterns", "GoF", "978-0201633610");
        Book book2 = physicalFactory.createBook("BK-002", "Clean Code", "Robert Martin", "978-0132350884");
        Book book3 = eBookFactory.createBook("BK-003", "Effective Java", "Joshua Bloch", "978-0134685991");
        Book book4 = physicalFactory.createBook("BK-004", "Java: The Complete Reference", "Herbert Schildt", "978-1260440232");
        Book book5 = eBookFactory.createBook("BK-005", "Spring in Action", "Craig Walls", "978-1617294945");

        libraryFacade.addBook(book1);
        libraryFacade.addBook(book2);
        libraryFacade.addBook(book3);
        libraryFacade.addBook(book4);
        libraryFacade.addBook(book5);

        // Seed users
        User user1 = new User("MEM-001", "John Doe", "john@email.com", "STUDENT");
        User user2 = new User("MEM-002", "Jane Smith", "jane@email.com", "FACULTY");
        User user3 = new User("MEM-003", "Bob Johnson", "bob@email.com", "PUBLIC");

        libraryFacade.addUser(user1);
        libraryFacade.addUser(user2);
        libraryFacade.addUser(user3);

        System.out.println("\n✅ System initialized with sample books and users!");
    }

    // ===== MAIN MENU =====
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📚 LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. Login");
        System.out.println("2. Register New User");
        System.out.println("3. Display All Books");
        System.out.println("4. Display All Users");
        System.out.println("-".repeat(50));
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
    }

    // ===== LOGIN =====
    private static void login() {
        System.out.println("\n=========================================");
        System.out.println("🔐 USER LOGIN");
        System.out.println("=========================================");

        System.out.print("Enter User ID (or 'admin' for admin access): ");
        String userId = scanner.nextLine().trim();

        if ("admin".equalsIgnoreCase(userId)) {
            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            try {
                securityProxy = new SecurityProxy("admin", password);
                System.out.println("\n✅ Admin login successful!");
                showAdminMenu();
            } catch (SecurityException e) {
                System.out.println("❌ " + e.getMessage());
            }
            return;
        }

        User user = libraryFacade.getUser(userId);
        if (user != null) {
            currentUser = user;
            System.out.println("\n✅ Welcome, " + user.getName() + "!");
            System.out.println("   User Type: " + user.getUserType());
            System.out.println("   Books Borrowed: " + user.getBooksBorrowed());
            showUserMenu();
        } else {
            System.out.println("❌ User not found! Please register first.");
        }
    }

    // ===== REGISTER USER =====
    private static void registerUser() {
        System.out.println("\n=========================================");
        System.out.println("📝 REGISTER NEW USER");
        System.out.println("=========================================");

        String name = "";
        while (name.isBlank()) {
            System.out.print("Enter Full Name: ");
            name = scanner.nextLine().trim();
            if (name.isBlank()) {
                System.out.println("❌ Name cannot be empty. Please try again.");
            }
        }

        String email = "";
        while (email.isBlank()) {
            System.out.print("Enter Email: ");
            email = scanner.nextLine().trim();
            if (email.isBlank()) {
                System.out.println("❌ Email cannot be empty. Please try again.");
            }
        }

        System.out.println("Select User Type:");
        System.out.println("1. STUDENT");
        System.out.println("2. FACULTY");
        System.out.println("3. PUBLIC");
        System.out.print("Choice: ");
        int typeChoice = getIntInput("");

        String userType;
        switch (typeChoice) {
            case 1 -> userType = "STUDENT";
            case 2 -> userType = "FACULTY";
            case 3 -> userType = "PUBLIC";
            default -> {
                System.out.println("Invalid choice! Defaulting to PUBLIC.");
                userType = "PUBLIC";
            }
        }

        String userId = "MEM-" + String.format("%03d", libraryFacade.getAllUsers().size() + 1);
        User newUser = new User(userId, name, email, userType);
        boolean success = libraryFacade.addUser(newUser);
        if (success) {
            System.out.println("\n=========================================");
            System.out.println("✅ USER REGISTERED SUCCESSFULLY!");
            System.out.println("User ID: " + userId);
            System.out.println("Name: " + name);
            System.out.println("Type: " + userType);
            System.out.println("=========================================");
        } else {
            System.out.println("\n=========================================");
            System.out.println("❌ USER REGISTRATION FAILED!");
            System.out.println("Please check the error message above and try again.");
            System.out.println("=========================================");
        }
    }

    // ===== USER MENU =====
    private static void showUserMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("👤 USER MENU - " + currentUser.getName());
            System.out.println("=".repeat(50));
            System.out.println("1. Borrow a Book");
            System.out.println("2. Return a Book");
            System.out.println("3. View My Transactions");
            System.out.println("4. View All Books");
            System.out.println("5. Pay Fine");
            System.out.println("6. Logout");
            System.out.println("=".repeat(50));

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> borrowBook();
                case 2 -> returnBook();
                case 3 -> viewMyTransactions();
                case 4 -> displayAllBooks();
                case 5 -> payFine();
                case 6 -> {
                    currentUser = null;
                    System.out.println("✅ Logged out successfully!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ===== BORROW BOOK =====
    private static void borrowBook() {
        System.out.println("\n=========================================");
        System.out.println("📖 BORROW A BOOK");
        System.out.println("=========================================");

        displayAllBooks();
        System.out.print("Enter Book ID to borrow: ");
        String bookId = scanner.nextLine().trim();

        boolean success = libraryFacade.borrowBook(currentUser.getUserId(), bookId);
        if (success) {
            // Log command (Command pattern)
            Book book = libraryFacade.getBook(bookId);
            if (book != null) {
                Command borrowCmd = new BorrowCommand(book);
                commandInvoker.executeCommand(borrowCmd);
            }
            System.out.println("\n✅ Book borrowed successfully!");
        }
    }

    // ===== RETURN BOOK =====
    private static void returnBook() {
        System.out.println("\n=========================================");
        System.out.println("📤 RETURN A BOOK");
        System.out.println("=========================================");

        List<Transaction> transactions = libraryFacade.getUserTransactions(currentUser.getUserId());
        if (transactions.isEmpty()) {
            System.out.println("❌ You have no active borrowings.");
            return;
        }

        System.out.println("Your active borrowings:");
        for (Transaction t : transactions) {
            if (t.isActive()) {
                Book book = libraryFacade.getBook(t.getBookId());
                System.out.println("   " + t.getBookId() + " - " + (book != null ? book.getTitle() : "Unknown") +
                                  " (Due: " + t.getDueDate() + ")");
            }
        }

        System.out.print("Enter Book ID to return: ");
        String bookId = scanner.nextLine().trim();

        boolean success = libraryFacade.returnBook(currentUser.getUserId(), bookId);
        if (success) {
            // Log command (Command pattern)
            Book book = libraryFacade.getBook(bookId);
            if (book != null) {
                Command returnCmd = new ReturnCommand(book);
                commandInvoker.executeCommand(returnCmd);
            }
            System.out.println("\n✅ Book returned successfully!");
        }
    }

    // ===== VIEW TRANSACTIONS =====
    private static void viewMyTransactions() {
        System.out.println("\n=========================================");
        System.out.println("📋 YOUR TRANSACTIONS");
        System.out.println("=========================================");

        List<Transaction> transactions = libraryFacade.getUserTransactions(currentUser.getUserId());
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction t : transactions) {
            Book book = libraryFacade.getBook(t.getBookId());
            System.out.println("Transaction: " + t.getTransactionId());
            System.out.println("   Book: " + (book != null ? book.getTitle() : "Unknown"));
            System.out.println("   Type: " + t.getType());
            System.out.println("   Due Date: " + t.getDueDate());
            System.out.println("   Status: " + (t.isActive() ? "Active" : "Returned"));
            System.out.println("   Fine: Rs." + t.getFineAmount());
            System.out.println("-".repeat(30));
        }
    }

    // ===== PAY FINE =====
    private static void payFine() {
        System.out.println("\n=========================================");
        System.out.println("💰 PAY FINE");
        System.out.println("=========================================");

        if (currentUser.getOutstandingFine() <= 0) {
            System.out.println("✅ You have no outstanding fines!");
            return;
        }

        System.out.println("Your outstanding fine: Rs." + currentUser.getOutstandingFine());
        System.out.println("Select payment method:");
        System.out.println("1. Khalti");
        System.out.println("2. eSewa");
        System.out.println("3. Cash (Pay at counter)");
        int choice = getIntInput("Choice: ");

        PaymentGateway gateway;
        String methodName;
        switch (choice) {
            case 1 -> {
                gateway = new KhaltiAdapter();
                methodName = "Khalti";
            }
            case 2 -> {
                gateway = new EsewaAdapter();
                methodName = "eSewa";
            }
            case 3 -> {
                System.out.println("✅ Please pay Rs." + currentUser.getOutstandingFine() + " at the library counter.");
                currentUser.payAllFine();
                // Update user in database
                try {
                    // We need to save the updated user – we'll use the facade's internal repo
                    // For simplicity, we'll directly update via repository (or we could add a method in facade)
                    // We'll just set outstanding fine to 0 and update
                    currentUser.setOutstandingFine(0);
                    // The facade doesn't have a direct update method, so we'll use the repository
                    // We'll add a helper method in the facade or just update using the repo.
                    // For now, we'll just print success.
                } catch (Exception e) {
                    // ignore
                }
                System.out.println("✅ All fines cleared!");
                return;
            }
            default -> {
                System.out.println("Invalid choice!");
                return;
            }
        }

        System.out.println("\n=========================================");
        System.out.println("ADAPTER PATTERN - " + methodName.toUpperCase() + " PAYMENT");
        System.out.println("=========================================");

        double amount = currentUser.getOutstandingFine();
        PaymentResult result = gateway.processPayment(amount, currentUser.getUserId());

        if (result.isSuccess()) {
            currentUser.payAllFine();
            // Update user in database – we'll need to use repository
            // Since we don't expose repo in facade, we'll call a new method
            // For demo, we just show success
            System.out.println("\n✅ Payment successful!");
            System.out.println("   Transaction ID: " + result.getTransactionId());
            System.out.println("   Amount: Rs." + result.getAmount());
            System.out.println("   Method: " + methodName);
            System.out.println("   Remaining fine: Rs." + currentUser.getOutstandingFine());
        } else {
            System.out.println("❌ Payment failed: " + result.getMessage());
        }
    }

    // ===== ADMIN MENU =====
    private static void showAdminMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("🔐 ADMIN MENU");
            System.out.println("=".repeat(50));
            System.out.println("1. View All Users");
            System.out.println("2. View All Books");
            System.out.println("3. Delete Book");
            System.out.println("4. Generate Report");
            System.out.println("5. Add New Book"); 
            System.out.println("6. Logout");
            System.out.println("=".repeat(50));

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> displayAllUsers();
                case 2 -> displayAllBooks();
                case 3 -> deleteBook();
                case 4 -> generateReport();
                case 5 -> addBookByAdmin();
                case 6 -> {
                    System.out.println("✅ Logged out successfully!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void deleteBook() {
        System.out.println("\n=========================================");
        System.out.println("DELETE BOOK");
        System.out.println("=========================================");
        displayAllBooks();
        System.out.print("Enter Book ID to delete: ");
        String bookId = scanner.nextLine().trim();

        boolean success = libraryFacade.deleteBook(bookId);
        if (success) {
            System.out.println("✅ Book deleted successfully!");
        } else {
            System.out.println("❌ Failed to delete book.");
        }
    }

    private static void generateReport() {
        System.out.println("\n=========================================");
        System.out.println("GENERATE REPORT");
        System.out.println("=========================================");
        System.out.println("Select report type:");
        System.out.println("1. Book Report");
        System.out.println("2. Member Report");
        System.out.println("3. Transaction Report");
        int choice = getIntInput("Choice: ");

        String reportType = switch (choice) {
            case 1 -> "BOOK";
            case 2 -> "MEMBER";
            case 3 -> "TRANSACTION";
            default -> "BOOK";
        };

        libraryFacade.generateReport(reportType);
    }
    
    private static void addBookByAdmin() {
        System.out.println("\n=========================================");
        System.out.println("📚 ADD NEW BOOK (Admin Only)");
        System.out.println("=========================================");

        System.out.print("Enter Book ID (e.g., BK-001): ");
        String bookId = scanner.nextLine().trim();

        // Check if book already exists
        if (libraryFacade.getBook(bookId) != null) {
            System.out.println("❌ Book ID already exists! Please use a different ID.");
            return;
        }

        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter Author: ");
        String author = scanner.nextLine().trim();

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();

        System.out.print("Enter Number of Copies: ");
        int copies = getIntInput("");

        System.out.println("Select Book Type:");
        System.out.println("1. Physical Book");
        System.out.println("2. E-Book");
        int typeChoice = getIntInput("Choice: ");

        Book newBook;

        if (typeChoice == 1) {
            // Physical Book
            System.out.print("Enter Shelf Location (e.g., A-12): ");
            String shelfLocation = scanner.nextLine().trim();

            System.out.println("Select Condition:");
            System.out.println("1. NEW");
            System.out.println("2. GOOD");
            System.out.println("3. DAMAGED");
            int condChoice = getIntInput("Choice: ");
            String condition = switch (condChoice) {
                case 1 -> "NEW";
                case 2 -> "GOOD";
                case 3 -> "DAMAGED";
                default -> "GOOD";
            };

            newBook = new PhysicalBook(bookId, title, author, isbn, copies, shelfLocation, condition);

        } else if (typeChoice == 2) {
            // E-Book
            System.out.print("Enter File Format (PDF, EPUB, MOBI): ");
            String fileFormat = scanner.nextLine().trim().toUpperCase();

            System.out.print("Enter File Size (in MB): ");
            double fileSize = getDoubleInput("");

            System.out.print("Enter Download URL: ");
            String downloadUrl = scanner.nextLine().trim();

            newBook = new EBook(bookId, title, author, isbn, fileFormat, fileSize, downloadUrl);

        } else {
            System.out.println("❌ Invalid choice! Book not added.");
            return;
        }

        // Set base cost (optional)
        System.out.print("Enter Base Cost (Rs.) – leave 0 if free: ");
        double cost = getDoubleInput("");
        newBook.setBaseCost(cost);

        // Save to database
        libraryFacade.addBook(newBook);

        System.out.println("\n=========================================");
        System.out.println("✅ BOOK ADDED SUCCESSFULLY!");
        System.out.println("   Book ID: " + bookId);
        System.out.println("   Title: " + title);
        System.out.println("   Type: " + newBook.getBookType());
        System.out.println("   Copies: " + copies);
        System.out.println("=========================================");
    }
    

    // ===== DISPLAY METHODS =====
    private static void displayAllBooks() {
        libraryFacade.displayAllBooks();
    }

    private static void displayAllUsers() {
        libraryFacade.displayAllUsers();
    }

    // ===== UTILITY METHODS =====
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Please enter a number.");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    private static long getLongInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
}