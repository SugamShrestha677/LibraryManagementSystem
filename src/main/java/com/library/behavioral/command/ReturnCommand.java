package com.library.behavioral.command;

import com.library.model.Book;

public class ReturnCommand implements Command {
    private Book book;
    private boolean executed;
    
    public ReturnCommand(Book book) {
        this.book = book;
        this.executed = false;
    }
    
    @Override
    public void execute() {
        if (book == null) {
            System.out.println("❌ Command Error: Book is null!");
            return;
        }
        
        book.returnBook();
        executed = true;
        System.out.println("   [Command] ReturnCommand executed for: " + book.getTitle());
        System.out.println("   [Command] Copies available: " + book.getCopiesAvailable());
    }
    
    @Override
    public void undo() {
        if (executed && book != null) {
            book.borrowBook();
            executed = false;
            System.out.println("   [Command] ReturnCommand undone for: " + book.getTitle());
            System.out.println("   [Command] Borrow restored: " + book.getCopiesAvailable());
        } else {
            System.out.println("⚠️ Command Error: Cannot undo ReturnCommand!");
        }
    }
    
    @Override
    public String getDescription() {
        return "RETURN - " + (book != null ? book.getTitle() : "Unknown Book");
    }
}