package com.library.creational.singleton;

public class LibraryConfig {
    private static LibraryConfig instance;
    
    private String libraryName;
    private int maxBooksPerMember;
    private double finePerDayStudent;
    private double finePerDayFaculty;
    private String operatingHours;
    
    // Private constructor
    private LibraryConfig() {
        this.libraryName = "Kathmandu University Library";
        this.maxBooksPerMember = 5;
        this.finePerDayStudent = 10.0;
        this.finePerDayFaculty = 20.0;
        this.operatingHours = "8:00 AM - 8:00 PM";
    }
    
    // Global access point
    public static LibraryConfig getInstance() {
        if (instance == null) {
            instance = new LibraryConfig();
        }
        return instance;
    }
    
    // Getters
    public String getLibraryName() { return libraryName; }
    public int getMaxBooksPerMember() { return maxBooksPerMember; }
    public double getFinePerDayStudent() { return finePerDayStudent; }
    public double getFinePerDayFaculty() { return finePerDayFaculty; }
    public String getOperatingHours() { return operatingHours; }
    
    // Setters (for flexibility)
    public void setLibraryName(String libraryName) { this.libraryName = libraryName; }
    public void setMaxBooksPerMember(int maxBooksPerMember) { this.maxBooksPerMember = maxBooksPerMember; }
    public void setFinePerDayStudent(double finePerDayStudent) { this.finePerDayStudent = finePerDayStudent; }
    public void setFinePerDayFaculty(double finePerDayFaculty) { this.finePerDayFaculty = finePerDayFaculty; }
    public void setOperatingHours(String operatingHours) { this.operatingHours = operatingHours; }
    
    public void displayConfig() {
        System.out.println("=========================================");
        System.out.println("SINGLETON PATTERN - LIBRARY CONFIG");
        System.out.println("=========================================");
        System.out.println("Library Name: " + libraryName);
        System.out.println("Max Books Per Member: " + maxBooksPerMember);
        System.out.println("Fine Per Day (Student): Rs. " + finePerDayStudent);
        System.out.println("Fine Per Day (Faculty): Rs. " + finePerDayFaculty);
        System.out.println("Operating Hours: " + operatingHours);
        System.out.println("=========================================");
    }
}