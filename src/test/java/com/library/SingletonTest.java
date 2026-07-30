package com.library;

import org.junit.jupiter.api.Test;

import com.library.creational.singleton.LibraryConfig;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class SingletonTest {

    @Test
    public void testSingletonInstance() {
        // Get two instances
        LibraryConfig config1 = LibraryConfig.getInstance();
        LibraryConfig config2 = LibraryConfig.getInstance();
        
        // They should be the same object
        assertSame(config1, config2, "Singleton instances should be the same");
    }

    @Test
    public void testSingletonConfiguration() {
        LibraryConfig config = LibraryConfig.getInstance();
        
        // Test configuration values
        assertEquals("Kathmandu University Library", config.getLibraryName());
        assertEquals(5, config.getMaxBooksPerMember());
        assertEquals(10.0, config.getFinePerDayStudent(), 0.01);
        assertEquals(20.0, config.getFinePerDayFaculty(), 0.01);
        assertEquals("8:00 AM - 8:00 PM", config.getOperatingHours());
    }

    @Test
    public void testSingletonModification() {
        LibraryConfig config = LibraryConfig.getInstance();
        
        // Modify config
        config.setLibraryName("New Library Name");
        config.setMaxBooksPerMember(10);
        
        // Get another instance and verify changes
        LibraryConfig config2 = LibraryConfig.getInstance();
        assertEquals("New Library Name", config2.getLibraryName());
        assertEquals(10, config2.getMaxBooksPerMember());
        
        // Reset for other tests
        config.setLibraryName("Kathmandu University Library");
        config.setMaxBooksPerMember(5);
    }
}