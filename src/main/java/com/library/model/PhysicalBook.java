package com.library.model;

public class PhysicalBook extends Book {
    private String shelfLocation;
    private String condition; // NEW, GOOD, DAMAGED

    public PhysicalBook(String bookId, String title, String author, String isbn, 
                        int copiesAvailable, String shelfLocation, String condition) {
        super(bookId, title, author, isbn, copiesAvailable);
        this.shelfLocation = shelfLocation;
        this.condition = condition;
        this.baseCost = 0.0;
    }

    @Override
    public String getBookType() {
        return "Physical";
    }

    @Override
    public String getLocation() {
        return "Shelf: " + shelfLocation;
    }

    public String getShelfLocation() { return shelfLocation; }
    public void setShelfLocation(String shelfLocation) { this.shelfLocation = shelfLocation; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    @Override
    public String toString() {
        return super.toString() + String.format(", Location=%s, Condition=%s", 
                shelfLocation, condition);
    }
}