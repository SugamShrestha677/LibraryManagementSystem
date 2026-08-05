package com.library;

import com.library.model.Book;
import com.library.model.PhysicalBook;
import com.library.behavioral.command.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTest {

    private Book book;
    private Book book2;
    private CommandInvoker invoker;

    @BeforeEach
    public void setUp() {
        book = new PhysicalBook("BK-TEST-001", "Design Patterns", "GoF",
                "ISBN-001", 3, "A-10", "NEW");
        book2 = new PhysicalBook("BK-TEST-002", "Clean Code", "Robert Martin",
                "ISBN-002", 2, "B-15", "NEW");
        invoker = new CommandInvoker();
    }

    @Test
    public void testBorrowCommand() {
        Command borrowCommand = new BorrowCommand(book);

        int initialCopies = book.getCopiesAvailable();
        borrowCommand.execute();

        assertEquals(initialCopies - 1, book.getCopiesAvailable());
        assertEquals(1, invoker.getCommandHistory().size());
    }

    @Test
    public void testReturnCommand() {
        // Borrow first
        book.borrowBook();
        int initialCopies = book.getCopiesAvailable();

        Command returnCommand = new ReturnCommand(book);
        returnCommand.execute();

        assertEquals(initialCopies + 1, book.getCopiesAvailable());
    }

    @Test
    public void testCommandHistory() {
        invoker.executeCommand(new BorrowCommand(book));
        invoker.executeCommand(new ReturnCommand(book));

        assertEquals(2, invoker.getCommandHistory().size());
        assertTrue(invoker.getCommandHistory().get(0).contains("BORROW"));
        assertTrue(invoker.getCommandHistory().get(1).contains("RETURN"));
    }

    @Test
    public void testUndoLastCommand() {
        int initialCopies = book.getCopiesAvailable();

        Command borrowCommand = new BorrowCommand(book);
        invoker.executeCommand(borrowCommand);
        assertEquals(initialCopies - 1, book.getCopiesAvailable());

        invoker.undoLastCommand();
        assertEquals(initialCopies, book.getCopiesAvailable());
        assertEquals(0, invoker.getCommandHistory().size());
    }

    @Test
    public void testUndoWhenHistoryEmpty() {
        assertEquals(0, invoker.getCommandHistory().size());
        assertDoesNotThrow(() -> invoker.undoLastCommand());
    }

    @Test
    public void testBorrowCommandUndo() {
        Command borrowCommand = new BorrowCommand(book);

        int initialCopies = book.getCopiesAvailable();
        borrowCommand.execute();
        assertEquals(initialCopies - 1, book.getCopiesAvailable());

        borrowCommand.undo();
        assertEquals(initialCopies, book.getCopiesAvailable());
    }

    @Test
    public void testMultipleCommandsWithHistory() {
        invoker.executeCommand(new BorrowCommand(book));
        invoker.executeCommand(new BorrowCommand(book2));
        invoker.executeCommand(new ReturnCommand(book));

        assertEquals(3, invoker.getCommandHistory().size());

        invoker.undoLastCommand();
        assertEquals(2, invoker.getCommandHistory().size());

        invoker.undoLastCommand();
        assertEquals(1, invoker.getCommandHistory().size());
    }

    @Test
    public void testBorrowUnavailableBook() {
        // Borrow all copies
        for (int i = 0; i < 3; i++) {
            book.borrowBook();
        }

        Command borrowCommand = new BorrowCommand(book);
        borrowCommand.execute();

        assertEquals(0, book.getCopiesAvailable());
    }

    @Test
    public void testCommandDescription() {
        Command borrowCommand = new BorrowCommand(book);
        assertNotNull(borrowCommand.getDescription());
        assertTrue(borrowCommand.getDescription().contains("BORROW"));

        Command returnCommand = new ReturnCommand(book);
        assertNotNull(returnCommand.getDescription());
        assertTrue(returnCommand.getDescription().contains("RETURN"));
    }
}