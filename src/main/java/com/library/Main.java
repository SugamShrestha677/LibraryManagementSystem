package com.library;

import com.library.model.*;
import com.library.creational.singleton.LibraryConfig;
import com.library.creational.builder.TransactionBuilder;
import com.library.creational.factory.*;
import com.library.structural.facade.LibraryFacade;
import com.library.structural.proxy.SecurityProxy;
import com.library.structural.decorator.*;
import com.library.structural.adapter.*;
import com.library.behavioral.strategy.*;
import com.library.behavioral.command.*;
import com.library.behavioral.state.*;
import com.library.behavioral.observer.*;

import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static LibraryFacade libraryFacade;
    private static User currentUser;
    private static SecurityProxy securityProxy;
    private static Map<String, Book> bookCache = new HashMap<>();
    private static CommandInvoker commandInvoker = new CommandInvoker();

    public static void main(String[] args) {
        // Initialize system
        initializeSystem();
        
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
                case 5 -> testSingleton();
                case 6 -> testFactoryMethod();
                case 7 -> testBuilderPattern();
                case 8 -> testStrategyPattern();
                case 9 -> testStatePattern();
                case 10 -> testObserverPattern();
                case 11 -> testDecoratorPattern();
                case 12 -> testAdapterPattern();
                case 13 -> testProxyPattern();
                case 14 -> testCommandPattern();
                case 15 -> testFacadePattern();
                case 0 -> {
                    System.out.println("\nThank you for using Library Management System!");
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void initializeSystem() {
        libraryFacade = new LibraryFacade();
        
        // Seed some books using Factory pattern
        BookFactory physicalFactory = new PhysicalBookFactory();
        BookFactory eBookFactory = new EBookFactory();
        
        Book book1 = physicalFactory.createBook("BK-001", "Design Patterns", "GoF", "978-0201633610");
        Book book2 = physicalFactory.createBook("BK-002", "Clean Code", "Robert Martin", "978-0132350884");
        Book book3 = eBookFactory.createBook("BK-003", "Effective Java", "Joshua Bloch", "978-0134685991");
        Book book4 = physicalFactory.createBook("BK-004", "Java: The Complete Reference", "Herbert Schildt", "978-1260440232");
        Book book5 = eBookFactory.createBook("BK-005", "Spring in Action", "Craig Walls", "978-1617294945");
        
        // Set costs for decorator testing
        book1.setBaseCost(0);
        book2.setBaseCost(0);
        book3.setBaseCost(0);
        book4.setBaseCost(0);
        book5.setBaseCost(0);
        
        libraryFacade.addBook(book1);
        libraryFacade.addBook(book2);
        libraryFacade.addBook(book3);
        libraryFacade.addBook(book4);
        libraryFacade.addBook(book5);
        
        // Seed some users
        User user1 = new User("MEM-001", "John Doe", "john@email.com", "STUDENT");
        User user2 = new User("MEM-002", "Jane Smith", "jane@email.com", "FACULTY");
        User user3 = new User("MEM-003", "Bob Johnson", "bob@email.com", "PUBLIC");
        
        libraryFacade.addUser(user1);
        libraryFacade.addUser(user2);
        libraryFacade.addUser(user3);
        
        System.out.println("\n✅ System initialized with sample books and users!");
    }

    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📚 LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. Login");
        System.out.println("2. Register New User");
        System.out.println("3. Display All Books");
        System.out.println("4. Display All Users");
        System.out.println("-".repeat(50));
        System.out.println("5. Test Singleton Pattern");
        System.out.println("6. Test Factory Method Pattern");
        System.out.println("7. Test Builder Pattern");
        System.out.println("8. Test Strategy Pattern");
        System.out.println("9. Test State Pattern");
        System.out.println("10. Test Observer Pattern");
        System.out.println("11. Test Decorator Pattern");
        System.out.println("12. Test Adapter Pattern");
        System.out.println("13. Test Proxy Pattern");
        System.out.println("14. Test Command Pattern");
        System.out.println("15. Test Facade Pattern");
        System.out.println("-".repeat(50));
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
    }

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
        
        // Try to find user
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

    private static void registerUser() {
        System.out.println("\n=========================================");
        System.out.println("📝 REGISTER NEW USER");
        System.out.println("=========================================");
        
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        
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
        libraryFacade.addUser(newUser);
        
        System.out.println("\n=========================================");
        System.out.println("✅ USER REGISTERED SUCCESSFULLY!");
        System.out.println("User ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Type: " + userType);
        System.out.println("=========================================");
    }

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

    private static void borrowBook() {
        System.out.println("\n=========================================");
        System.out.println("📖 BORROW A BOOK");
        System.out.println("=========================================");
        
        displayAllBooks();
        System.out.print("Enter Book ID to borrow: ");
        String bookId = scanner.nextLine().trim();
        
        // Use Facade pattern
        boolean success = libraryFacade.borrowBook(currentUser.getUserId(), bookId);
        
        if (success) {
            // Use Command pattern
            Book book = libraryFacade.getBook(bookId);
            Command borrowCommand = new BorrowCommand(book);
            commandInvoker.executeCommand(borrowCommand);
            System.out.println("\n✅ Book borrowed successfully!");
        }
    }

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
        
        // Use Facade pattern
        boolean success = libraryFacade.returnBook(currentUser.getUserId(), bookId);
        
        if (success) {
            // Use Command pattern
            Book book = libraryFacade.getBook(bookId);
            Command returnCommand = new ReturnCommand(book);
            commandInvoker.executeCommand(returnCommand);
        }
    }

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
            System.out.println("   Date: " + t.getDueDate());
            System.out.println("   Status: " + (t.isActive() ? "Active" : "Returned"));
            System.out.println("   Fine: Rs." + t.getFineAmount());
            System.out.println("-".repeat(30));
        }
    }

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
        System.out.println("3. Cash");
        int choice = getIntInput("Choice: ");
        
        PaymentGateway gateway;
        switch (choice) {
            case 1 -> gateway = new KhaltiAdapter();
            case 2 -> gateway = new EsewaAdapter();
            case 3 -> {
                System.out.println("Pay fine at the library counter.");
                return;
            }
            default -> {
                System.out.println("Invalid choice!");
                return;
            }
        }
        
        System.out.println("\n=========================================");
        System.out.println("ADAPTER PATTERN - PAYMENT PROCESSING");
        System.out.println("=========================================");
        PaymentResult result = gateway.processPayment(currentUser.getOutstandingFine(), 
                                                     currentUser.getUserId());
        
        if (result.isSuccess()) {
            currentUser.payFine(currentUser.getOutstandingFine());
            System.out.println("\n✅ Payment successful!");
            System.out.println("   Transaction ID: " + result.getTransactionId());
            System.out.println("   Amount: Rs." + result.getAmount());
            System.out.println("   Remaining fine: Rs." + currentUser.getOutstandingFine());
        } else {
            System.out.println("❌ Payment failed: " + result.getMessage());
        }
    }

    private static void showAdminMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("🔐 ADMIN MENU");
            System.out.println("=".repeat(50));
            System.out.println("1. View All Users");
            System.out.println("2. View All Books");
            System.out.println("3. Delete Book (Proxy Pattern Demo)");
            System.out.println("4. Generate Report (Proxy Pattern Demo)");
            System.out.println("5. Test Proxy Pattern");
            System.out.println("6. Logout");
            System.out.println("=".repeat(50));
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> displayAllUsers();
                case 2 -> displayAllBooks();
                case 3 -> deleteBook();
                case 4 -> generateReport();
                case 5 -> testProxyPattern();
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
        System.out.println("DELETE BOOK (Proxy Pattern)");
        System.out.println("=========================================");
        displayAllBooks();
        System.out.print("Enter Book ID to delete: ");
        String bookId = scanner.nextLine().trim();
        
        securityProxy.deleteBook(bookId);
    }

    private static void generateReport() {
        System.out.println("\n=========================================");
        System.out.println("GENERATE REPORT (Proxy Pattern)");
        System.out.println("=========================================");
        System.out.println("Select report type:");
        System.out.println("1. Member Report");
        System.out.println("2. Book Report");
        System.out.println("3. Transaction Report");
        int choice = getIntInput("Choice: ");
        
        String reportType = switch (choice) {
            case 1 -> "Member";
            case 2 -> "Book";
            case 3 -> "Transaction";
            default -> "General";
        };
        
        securityProxy.generateReport(reportType);
    }

    private static void displayAllBooks() {
        libraryFacade.displayAllBooks();
    }

    private static void displayAllUsers() {
        libraryFacade.displayAllUsers();
    }

    // ===== PATTERN TEST METHODS =====

    private static void testSingleton() {
        System.out.println("\n=========================================");
        System.out.println("🔷 SINGLETON PATTERN TEST");
        System.out.println("=========================================");
        
        LibraryConfig config1 = LibraryConfig.getInstance();
        LibraryConfig config2 = LibraryConfig.getInstance();
        
        System.out.println("Config1 Instance: " + config1.hashCode());
        System.out.println("Config2 Instance: " + config2.hashCode());
        System.out.println("Are they the same? " + (config1 == config2));
        System.out.println("\n✅ Singleton Pattern working correctly!");
        System.out.println("   Both references point to the same instance.");
        
        System.out.println("\nCurrent Configuration:");
        config1.displayConfig();
    }

    private static void testFactoryMethod() {
        System.out.println("\n=========================================");
        System.out.println("🔷 FACTORY METHOD PATTERN TEST");
        System.out.println("=========================================");
        
        System.out.println("Select book type to create:");
        System.out.println("1. Physical Book");
        System.out.println("2. E-Book");
        int choice = getIntInput("Choice: ");
        
        BookFactory factory;
        String bookType;
        if (choice == 1) {
            factory = new PhysicalBookFactory();
            bookType = "Physical";
        } else {
            factory = new EBookFactory();
            bookType = "E-Book";
        }
        
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();
        
        String bookId = "BK-" + String.format("%03d", libraryFacade.getAllBooks().size() + 1);
        Book book = factory.createBook(bookId, title, author, isbn);
        libraryFacade.addBook(book);
        
        System.out.println("\n=========================================");
        System.out.println("✅ Book created successfully!");
        System.out.println("   Book ID: " + bookId);
        System.out.println("   Title: " + book.getTitle());
        System.out.println("   Type: " + bookType);
        System.out.println("   Location: " + book.getLocation());
        System.out.println("=========================================");
    }

    private static void testBuilderPattern() {
        System.out.println("\n=========================================");
        System.out.println("🔷 BUILDER PATTERN TEST");
        System.out.println("=========================================");
        
        System.out.println("Building a Transaction with Builder Pattern...");
        
        String transactionId = "TR-" + String.format("%03d", new Random().nextInt(999));
        
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine().trim();
        
        System.out.println("Add optional features:");
        System.out.print("Priority processing? (y/n): ");
        boolean priority = scanner.nextLine().trim().equalsIgnoreCase("y");
        System.out.print("Add insurance? (y/n): ");
        boolean insurance = scanner.nextLine().trim().equalsIgnoreCase("y");
        
        Transaction transaction = new TransactionBuilder()
                .setTransactionId(transactionId)
                .setMemberId(memberId)
                .setBookId(bookId)
                .setType("BORROW")
                .setPriority(priority)
                .setInsurance(insurance)
                .build();
        
        System.out.println("\n=========================================");
        System.out.println("✅ Transaction built successfully!");
        System.out.println("   Transaction ID: " + transaction.getTransactionId());
        System.out.println("   Member ID: " + transaction.getMemberId());
        System.out.println("   Book ID: " + transaction.getBookId());
        System.out.println("   Due Date: " + transaction.getDueDate());
        System.out.println("   Priority: " + (transaction.isPriority() ? "Yes" : "No"));
        System.out.println("   Insurance: " + (transaction.isInsurance() ? "Yes" : "No"));
        System.out.println("=========================================");
    }

    private static void testStrategyPattern() {
        System.out.println("\n=========================================");
        System.out.println("🔷 STRATEGY PATTERN TEST");
        System.out.println("=========================================");
        
        System.out.println("Select member type for fine calculation:");
        System.out.println("1. STUDENT");
        System.out.println("2. FACULTY");
        int choice = getIntInput("Choice: ");
        
        FineStrategy strategy;
        String type;
        if (choice == 1) {
            strategy = new StudentFineStrategy();
            type = "STUDENT";
        } else {
            strategy = new FacultyFineStrategy();
            type = "FACULTY";
        }
        
        System.out.print("Enter number of overdue days: ");
        long days = getLongInput("");
        
        double fine = strategy.calculateFine(days);
        
        System.out.println("\n=========================================");
        System.out.println("📊 FINE CALCULATION RESULT");
        System.out.println("=========================================");
        System.out.println("Member Type: " + type);
        System.out.println("Overdue Days: " + days);
        System.out.println("Fine Amount: Rs." + fine);
        System.out.println("=========================================");
    }

    private static void testStatePattern() {
        System.out.println("\n=========================================");
        System.out.println("🔷 STATE PATTERN TEST");
        System.out.println("=========================================");
        
        System.out.print("Enter Book ID to test state transitions: ");
        String bookId = scanner.nextLine().trim();
        Book book = libraryFacade.getBook(bookId);
        
        if (book == null) {
            System.out.println("❌ Book not found!");
            return;
        }
        
        // Create state context
        BookStateContext context = new BookStateContext(book);
        
        System.out.println("\nBook: " + book.getTitle());
        System.out.println("Current Status: " + context.getCurrentStatus());
        System.out.println("\nSimulating status changes...");
        
        while (true) {
            System.out.println("\nSelect operation:");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. Reserve Book");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. View Current Status");
            System.out.println("6. Exit State Demo");
            int choice = getIntInput("Choice: ");
            
            switch (choice) {
                case 1 -> {
                    context.borrow();
                    System.out.println("   Status: " + context.getCurrentStatus());
                }
                case 2 -> {
                    context.returnBook();
                    System.out.println("   Status: " + context.getCurrentStatus());
                }
                case 3 -> {
                    context.reserve();
                    System.out.println("   Status: " + context.getCurrentStatus());
                }
                case 4 -> {
                    context.cancelReservation();
                    System.out.println("   Status: " + context.getCurrentStatus());
                }
                case 5 -> System.out.println("   Current Status: " + context.getCurrentStatus());
                case 6 -> {
                    System.out.println("Exiting State Demo...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void testObserverPattern() {
        System.out.println("\n=========================================");
        System.out.println("🔷 OBSERVER PATTERN TEST");
        System.out.println("=========================================");
        
        System.out.print("Enter Book ID to observe: ");
        String bookId = scanner.nextLine().trim();
        Book book = libraryFacade.getBook(bookId);
        
        if (book == null) {
            System.out.println("❌ Book not found!");
            return;
        }
        
        BookStatus bookStatus = new BookStatus(bookId, book.getTitle());
        NotificationService notificationService = new NotificationService();
        
        // Attach observer
        bookStatus.attach(notificationService);
        
        System.out.println("\nObserver attached to: " + book.getTitle());
        System.out.println("\nSimulating status changes...");
        System.out.println("1. Change status to AVAILABLE");
        System.out.println("2. Change status to BORROWED");
        System.out.println("3. Change status to RESERVED");
        System.out.println("4. Change status to LOST");
        int choice = getIntInput("Choice: ");
        
        String status = switch (choice) {
            case 1 -> "AVAILABLE";
            case 2 -> "BORROWED";
            case 3 -> "RESERVED";
            case 4 -> "LOST";
            default -> "AVAILABLE";
        };
        
        bookStatus.setStatus(status);
        
        System.out.println("\n✅ Observer pattern demonstrated!");
        System.out.println("   All observers were notified of the status change.");
    }

    private static void testDecoratorPattern() {
        System.out.println("\n=========================================");
        System.out.println("🔷 DECORATOR PATTERN TEST");
        System.out.println("=========================================");
        
        System.out.print("Enter Book ID to decorate: ");
        String bookId = scanner.nextLine().trim();
        Book book = libraryFacade.getBook(bookId);
        
        if (book == null) {
            System.out.println("❌ Book not found!");
            return;
        }
        
        System.out.println("\nBase Book: " + book.getTitle());
        System.out.println("Base Cost: Rs." + book.getBaseCost());
        
        // Start with base book
        Book decoratedBook = book;
        
        while (true) {
            System.out.println("\nCurrent: " + decoratedBook.getTitle());
            System.out.println("Cost: Rs." + decoratedBook.getBaseCost());
            System.out.println("\nAdd features (select multiple):");
            System.out.println("1. Add Insurance (Rs.50)");
            System.out.println("2. Add Priority Processing (Rs.100)");
            System.out.println("3. View Final Decorated Book");
            System.out.println("4. Exit Decorator Demo");
            int choice = getIntInput("Choice: ");
            
            switch (choice) {
                case 1 -> {
                    decoratedBook = new InsuranceDecorator(decoratedBook);
                    System.out.println("✅ Insurance added!");
                }
                case 2 -> {
                    decoratedBook = new PriorityDecorator(decoratedBook);
                    System.out.println("✅ Priority Processing added!");
                }
                case 3 -> {
                    System.out.println("\n=========================================");
                    System.out.println("📦 DECORATED BOOK RESULT");
                    System.out.println("=========================================");
                    System.out.println("Description: " + decoratedBook.getTitle());
                    System.out.println("Total Cost: Rs." + decoratedBook.getBaseCost());
                    System.out.println("=========================================");
                }
                case 4 -> {
                    System.out.println("Exiting Decorator Demo...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void testAdapterPattern() {
        System.out.println("\n=========================================");
        System.out.println("🔷 ADAPTER PATTERN TEST");
        System.out.println("=========================================");
        
        System.out.println("Select payment gateway:");
        System.out.println("1. Khalti");
        System.out.println("2. eSewa");
        int choice = getIntInput("Choice: ");
        
        PaymentGateway gateway;
        String name;
        if (choice == 1) {
            gateway = new KhaltiAdapter();
            name = "Khalti";
        } else {
            gateway = new EsewaAdapter();
            name = "eSewa";
        }
        
        System.out.print("Enter amount (Rs.): ");
        double amount = getDoubleInput("");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine().trim();
        
        System.out.println("\n=========================================");
        System.out.println("🔄 PROCESSING PAYMENT WITH " + name.toUpperCase());
        System.out.println("=========================================");
        
        PaymentResult result = gateway.processPayment(amount, userId);
        
        System.out.println("\nPayment Result:");
        System.out.println("   Status: " + (result.isSuccess() ? "✅ SUCCESS" : "❌ FAILED"));
        System.out.println("   Transaction ID: " + result.getTransactionId());
        System.out.println("   Amount: Rs." + result.getAmount());
        System.out.println("   Message: " + result.getMessage());
        System.out.println("=========================================");
    }

    private static void testProxyPattern() {
        System.out.println("\n=========================================");
        System.out.println("🔷 PROXY PATTERN TEST");
        System.out.println("=========================================");
        
        System.out.println("Testing Security Proxy...");
        System.out.println("\nScenario 1: Admin access");
        try {
            SecurityProxy adminProxy = new SecurityProxy("admin", "admin123");
            adminProxy.viewAllUsers();
            System.out.println("✅ Admin access granted!");
        } catch (SecurityException e) {
            System.out.println("❌ " + e.getMessage());
        }
        
        System.out.println("\nScenario 2: Invalid credentials");
        try {
            SecurityProxy invalidProxy = new SecurityProxy("invalid", "wrong");
            invalidProxy.viewAllUsers();
        } catch (SecurityException e) {
            System.out.println("❌ " + e.getMessage());
            System.out.println("✅ Security proxy blocked invalid access!");
        }
        
        System.out.println("\nScenario 3: Member attempting admin operation");
        try {
            SecurityProxy memberProxy = new SecurityProxy("member", "member123");
            memberProxy.deleteBook("BK-001");
        } catch (SecurityException e) {
            System.out.println("❌ " + e.getMessage());
            System.out.println("✅ Security proxy blocked unauthorized operation!");
        }
    }

    private static void testCommandPattern() {
        System.out.println("\n=========================================");
        System.out.println("🔷 COMMAND PATTERN TEST");
        System.out.println("=========================================");
        
        System.out.println("Command history size: " + commandInvoker.getCommandHistory().size());
        System.out.println("\nDemonstrating command execution and history...");
        
        System.out.println("Execute some commands:");
        System.out.println("1. Borrow Command");
        System.out.println("2. Return Command");
        System.out.println("3. View Command History");
        System.out.println("4. Undo Last Command");
        System.out.println("5. Exit Command Demo");
        
        while (true) {
            int choice = getIntInput("Choice: ");
            
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Book ID to borrow: ");
                    String bookId = scanner.nextLine().trim();
                    Book book = libraryFacade.getBook(bookId);
                    if (book != null) {
                        Command borrowCmd = new BorrowCommand(book);
                        commandInvoker.executeCommand(borrowCmd);
                        System.out.println("✅ Borrow command executed!");
                    } else {
                        System.out.println("❌ Book not found!");
                    }
                }
                case 2 -> {
                    System.out.print("Enter Book ID to return: ");
                    String bookId = scanner.nextLine().trim();
                    Book book = libraryFacade.getBook(bookId);
                    if (book != null) {
                        Command returnCmd = new ReturnCommand(book);
                        commandInvoker.executeCommand(returnCmd);
                        System.out.println("✅ Return command executed!");
                    } else {
                        System.out.println("❌ Book not found!");
                    }
                }
                case 3 -> {
                    System.out.println("\nCommand History:");
                    for (String history : commandInvoker.getCommandHistory()) {
                        System.out.println("   " + history);
                    }
                    System.out.println("Total commands: " + commandInvoker.getCommandHistory().size());
                }
                case 4 -> {
                    commandInvoker.undoLastCommand();
                }
                case 5 -> {
                    System.out.println("Exiting Command Demo...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void testFacadePattern() {
        System.out.println("\n=========================================");
        System.out.println("🔷 FACADE PATTERN TEST");
        System.out.println("=========================================");
        
        System.out.println("The Facade pattern simplifies complex operations.");
        System.out.println("\nWhen you borrow a book, the facade handles:");
        System.out.println("  1. Checking user existence");
        System.out.println("  2. Checking book availability");
        System.out.println("  3. Checking borrowing limits");
        System.out.println("  4. Creating transactions");
        System.out.println("  5. Updating book status");
        System.out.println("  6. Updating user records");
        System.out.println("  7. Sending notifications");
        System.out.println("\nAll of this is done with one method call: borrowBook()");
        
        System.out.print("\nEnter User ID to test: ");
        String userId = scanner.nextLine().trim();
        System.out.print("Enter Book ID to borrow: ");
        String bookId = scanner.nextLine().trim();
        
        boolean success = libraryFacade.borrowBook(userId, bookId);
        if (!success) {
            System.out.println("\n❌ Borrow failed. Check if user and book exist.");
        }
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