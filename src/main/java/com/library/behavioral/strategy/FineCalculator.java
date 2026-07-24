package com.library.behavioral.strategy;

import com.library.model.User;

public class FineCalculator {
    private FineStrategy strategy;
    private User user;
    
    public FineCalculator(User user) {
        this.user = user;
        // Set default strategy based on user type
        if ("STUDENT".equals(user.getUserType())) {
            this.strategy = new StudentFineStrategy();
        } else if ("FACULTY".equals(user.getUserType())) {
            this.strategy = new FacultyFineStrategy();
        } else {
            // Default to student rate for others
            this.strategy = new StudentFineStrategy();
        }
    }
    
    public void setStrategy(FineStrategy strategy) {
        this.strategy = strategy;
        System.out.println("   [Strategy] Fine strategy changed to: " + strategy.getClass().getSimpleName());
    }
    
    public double calculateFine(long overdueDays) {
        if (strategy == null) {
            System.out.println("❌ No strategy set!");
            return 0;
        }
        
        System.out.println("\n=========================================");
        System.out.println("📊 STRATEGY PATTERN - FINE CALCULATION");
        System.out.println("=========================================");
        System.out.println("User: " + user.getName());
        System.out.println("User Type: " + user.getUserType());
        System.out.println("Strategy: " + strategy.getClass().getSimpleName());
        System.out.println("-".repeat(40));
        
        double fine = strategy.calculateFine(overdueDays);
        
        System.out.println("-".repeat(40));
        System.out.println("Total Fine: Rs." + fine);
        System.out.println("=========================================");
        
        return fine;
    }
}