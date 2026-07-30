package com.library;

import com.library.model.User;
import com.library.behavioral.strategy.FacultyFineStrategy;
import com.library.behavioral.strategy.FineCalculator;
import com.library.creational.singleton.LibraryConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StrategyTest {

    private LibraryConfig config;

    @BeforeEach
    public void setup() {
        config = LibraryConfig.getInstance();
        config.setFinePerDayStudent(10.0);
        config.setFinePerDayFaculty(20.0);
    }

    @Test
    public void testStudentFineStrategy() {
        FineStrategy strategy = new StudentFineStrategy();
        double fine = strategy.calculateFine(3); // 3 days overdue
        
        assertEquals(30.0, fine, 0.01); // Rs. 10/day * 3 days
    }

    @Test
    public void testFacultyFineStrategy() {
        FineStrategy strategy = new FacultyFineStrategy();
        double fine = strategy.calculateFine(3); // 3 days overdue
        
        assertEquals(60.0, fine, 0.01); // Rs. 20/day * 3 days
    }

    @Test
    public void testNoFineForOnTimeReturn() {
        FineStrategy strategy = new StudentFineStrategy();
        double fine = strategy.calculateFine(0); // No overdue days
        
        assertEquals(0.0, fine, 0.01);
    }

    @Test
    public void testNoFineForNegativeDays() {
        FineStrategy strategy = new StudentFineStrategy();
        double fine = strategy.calculateFine(-1); // Negative days
        
        assertEquals(0.0, fine, 0.01);
    }

    @Test
    public void testFineCalculatorWithDifferentStrategies() {
        User student = new User("MEM-001", "John", "john@email.com", "STUDENT");
        User faculty = new User("MEM-002", "Jane", "jane@email.com", "FACULTY");
        
        FineCalculator studentCalc = new FineCalculator(student);
        FineCalculator facultyCalc = new FineCalculator(faculty);
        
        double studentFine = studentCalc.calculateFine(5);
        double facultyFine = facultyCalc.calculateFine(5);
        
        assertEquals(50.0, studentFine, 0.01);
        assertEquals(100.0, facultyFine, 0.01);
    }

    @Test
    public void testStrategyChangeAtRuntime() {
        User user = new User("MEM-003", "Bob", "bob@email.com", "STUDENT");
        FineCalculator calculator = new FineCalculator(user);
        
        // Default student strategy
        double fine1 = calculator.calculateFine(2);
        assertEquals(20.0, fine1, 0.01);
        
        // Change to faculty strategy
        calculator.setStrategy(new FacultyFineStrategy());
        double fine2 = calculator.calculateFine(2);
        assertEquals(40.0, fine2, 0.01);
    }
}