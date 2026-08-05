package com.library;

import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.PhysicalBook;
import com.library.structural.decorator.InsuranceDecorator;
import com.library.structural.decorator.PriorityDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecoratorTest {

    @Test
    public void testBookWithInsurance() {
        // Create base book
        Book baseBook = new PhysicalBook("BK-001", "Clean Code", "Robert Martin",
                "ISBN-001", 3, "A-10", "NEW");
        baseBook.setBaseCost(0);

        // Decorate with insurance
        Book insuredBook = new InsuranceDecorator(baseBook);

        // Verify cost and description
        assertEquals(50.0, insuredBook.getCost(), 0.01);
        assertTrue(insuredBook.toString().contains("Insurance"));
        assertEquals("Physical + Insurance", insuredBook.getBookType());
    }

    @Test
    public void testBookWithPriority() {
        // Create base book
        Book baseBook = new EBook("BK-002", "Design Patterns", "GoF",
                "ISBN-002", "PDF", 5.2, "http://download.com");
        baseBook.setBaseCost(0);

        // Decorate with priority
        Book priorityBook = new PriorityDecorator(baseBook);

        // Verify cost and description
        assertEquals(100.0, priorityBook.getCost(), 0.01);
        assertTrue(priorityBook.toString().contains("Priority"));
        assertEquals("E-Book + Priority", priorityBook.getBookType());
    }

    @Test
    public void testBookWithMultipleDecorators() {
        // Create base book
        Book baseBook = new PhysicalBook("BK-003", "Java Complete", "Author",
                "ISBN-003", 2, "B-20", "NEW");
        baseBook.setBaseCost(0);

        // Apply multiple decorators
        Book decoratedBook = new PriorityDecorator(
                new InsuranceDecorator(baseBook)
        );

        // Verify total cost
        assertEquals(150.0, decoratedBook.getCost(), 0.01);
        assertTrue(decoratedBook.toString().contains("Insurance"));
        assertTrue(decoratedBook.toString().contains("Priority"));
        assertEquals("Physical + Insurance + Priority", decoratedBook.getBookType());
    }

    @Test
    public void testDecoratorOrderDoesNotAffectCost() {
        Book baseBook = new PhysicalBook("BK-004", "Python Programming", "Author",
                "ISBN-004", 1, "C-30", "NEW");
        baseBook.setBaseCost(0);

        // Different order should give same total
        Book decorated1 = new PriorityDecorator(new InsuranceDecorator(baseBook));
        Book decorated2 = new InsuranceDecorator(new PriorityDecorator(baseBook));

        assertEquals(decorated1.getCost(), decorated2.getCost(), 0.01);
        assertEquals(150.0, decorated1.getCost(), 0.01);
    }

    @Test
    public void testEBookWithDecorators() {
        Book ebook = new EBook("BK-005", "Spring Framework", "Author",
                "ISBN-005", "EPUB", 3.5, "http://download.com");
        ebook.setBaseCost(0);

        Book decoratedEbook = new InsuranceDecorator(ebook);

        assertEquals(50.0, decoratedEbook.getCost(), 0.01);
        assertEquals("E-Book + Insurance", decoratedEbook.getBookType());
    }
}