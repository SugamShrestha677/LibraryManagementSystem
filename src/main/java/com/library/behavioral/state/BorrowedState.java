package com.library.behavioral.state;

public class BorrowedState implements BookState {
    @Override
    public void enter(BookStateContext context) {
        System.out.println("📖 Book is BORROWED");
    }
    
    @Override
    public void handleBorrow(BookStateContext context) {
        System.out.println("⚠️ Book is already borrowed. Cannot borrow again.");
    }
    
    @Override
    public void handleReturn(BookStateContext context) {
        System.out.println("✅ Returning book...");
        context.setState(new AvailableState());
    }
    
    @Override
    public void handleReserve(BookStateContext context) {
        System.out.println("✅ Reserving book for next available...");
        context.setState(new ReservedState());
    }
    
    @Override
    public void handleCancelReservation(BookStateContext context) {
        System.out.println("⚠️ Book is not reserved. Cannot cancel.");
    }
    
    @Override
    public String getStatus() {
        return "BORROWED";
    }
}