package com.library;

import com.library.behavioral.observer.BookStatus;
import com.library.behavioral.observer.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverTest {

    private BookStatus bookStatus;
    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        // Create a book status subject and a notification observer
        bookStatus = new BookStatus("BK-001", "Design Patterns");
        notificationService = new NotificationService();
    }

    @Test
    public void testAttachAndNotify() {
        // Attach observer
        bookStatus.attach(notificationService);
        assertEquals(1, bookStatus.getObservers().size());

        // Change status – should trigger notification
        bookStatus.setStatus("BORROWED");

        // Verify that the observer was notified
        assertTrue(notificationService.wasNotified());
        assertNotNull(notificationService.getLastNotification());
        assertTrue(notificationService.getLastNotification().contains("Design Patterns"));
        assertTrue(notificationService.getLastNotification().contains("BORROWED"));
    }

    @Test
    public void testMultipleObservers() {
        NotificationService observer2 = new NotificationService();

        bookStatus.attach(notificationService);
        bookStatus.attach(observer2);
        assertEquals(2, bookStatus.getObservers().size());

        bookStatus.setStatus("AVAILABLE");

        assertTrue(notificationService.wasNotified());
        assertTrue(observer2.wasNotified());
    }

    @Test
    public void testDetachObserver() {
        bookStatus.attach(notificationService);
        assertEquals(1, bookStatus.getObservers().size());

        bookStatus.detach(notificationService);
        assertEquals(0, bookStatus.getObservers().size());

        // Reset notification flag and change status
        notificationService.resetNotification();
        bookStatus.setStatus("RESERVED");

        // Observer should NOT have been notified
        assertFalse(notificationService.wasNotified());
    }

    @Test
    public void testObserverReceivesCorrectMessage() {
        bookStatus.attach(notificationService);

        String newStatus = "BORROWED";
        bookStatus.setStatus(newStatus);

        String notification = notificationService.getLastNotification();
        assertNotNull(notification);
        assertTrue(notification.contains("Design Patterns"));
        assertTrue(notification.contains("BORROWED"));
    }

    @Test
    public void testMultipleStatusChanges() {
        bookStatus.attach(notificationService);

        bookStatus.setStatus("AVAILABLE");
        assertTrue(notificationService.wasNotified());

        notificationService.resetNotification();
        bookStatus.setStatus("BORROWED");
        assertTrue(notificationService.wasNotified());

        notificationService.resetNotification();
        bookStatus.setStatus("RESERVED");
        assertTrue(notificationService.wasNotified());
    }

    @Test
    public void testNoNotificationWhenNoObservers() {
        // No observer attached
        bookStatus.setStatus("BORROWED");
        // No exception should be thrown
        // The notificationService was never attached, so it should not be notified
        assertFalse(notificationService.wasNotified());
    }
}