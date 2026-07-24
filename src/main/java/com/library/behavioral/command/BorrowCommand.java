package com.library.behavioral.command;

import com.library.model.Book;

public class BorrowCommand implements Command {
    private Book book;
    private boolean executed;
    
    public BorrowCommand(Book book) {
        this.book = book;
        this.executed = false;
    }
    
    @Override
    public void execute() {
        if (book == null) {
            System.out.println("❌ Command Error: Book is null!");
            return;
        }
        
        if (book.isAvailable()) {
            book.borrowBook();
            executed = true;
            System.out.println("   [Command] BorrowCommand executed for: " + book.getTitle());
            System.out.println("   [Command] Copies remaining: " + book.getCopiesAvailable());
        } else {
            System.out.println("❌ Command Error: Book is not available!");
        }
    }
    
    @Override
    public void undo() {
        if (executed && book != null) {
            book.returnBook();
            executed = false;
            System.out.println("   [Command] BorrowCommand undone for: " + book.getTitle());
            System.out.println("   [Command] Copies restored: " + book.getCopiesAvailable());
        } else {
            System.out.println("⚠️ Command Error: Cannot undo BorrowCommand!");
        }
    }
    
    @Override
    public String getDescription() {
        return "BORROW - " + (book != null ? book.getTitle() : "Unknown Book");
    }
}