package com.library.behavioral.strategy;

import com.library.creational.singleton.LibraryConfig;

public class StudentFineStrategy implements FineStrategy {
    @Override
    public double calculateFine(long overdueDays) {
        if (overdueDays <= 0) return 0.0;
        LibraryConfig config = LibraryConfig.getInstance();
        return overdueDays * config.getFinePerDayStudent();
    }
}