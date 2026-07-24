package com.library.behavioral.state;

public class AvailableState implements BookState {
    @Override
    public void enter(BookStateContext context) {
        System.out.println("📚 Book is AVAILABLE");
    }
    
    @Override
    public void handleBorrow(BookStateContext context) {
        System.out.println("✅ Borrowing book...");
        context.setState(new BorrowedState());
    }
    
    @Override
    public void handleReturn(BookStateContext context) {
        System.out.println("⚠️ Book is already available. Cannot return.");
    }
    
    @Override
    public void handleReserve(BookStateContext context) {
        System.out.println("✅ Reserving book...");
        context.setState(new ReservedState());
    }
    
    @Override
    public void handleCancelReservation(BookStateContext context) {
        System.out.println("⚠️ Book is not reserved. Cannot cancel.");
    }
    
    @Override
    public String getStatus() {
        return "AVAILABLE";
    }
}