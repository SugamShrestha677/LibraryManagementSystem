package com.library.behavioral.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationService implements Observer {
    private String lastNotification;
    private boolean wasNotified;
    
    public NotificationService() {
        this.wasNotified = false;
        this.lastNotification = "";
    }
    
    @Override
    public void update(String eventType, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        System.out.println("\n=========================================");
        System.out.println("🔔 NOTIFICATION SERVICE (OBSERVER PATTERN)");
        System.out.println("=========================================");
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Event Type: " + eventType);
        System.out.println("Message: " + message);
        System.out.println("Status: ✅ Notification Sent!");
        System.out.println("=========================================");
        
        this.wasNotified = true;
        this.lastNotification = message;
    }
    
    public boolean wasNotified() {
        return wasNotified;
    }
    
    public String getLastNotification() {
        return lastNotification;
    }
    
    public void resetNotification() {
        this.wasNotified = false;
        this.lastNotification = "";
    }
}