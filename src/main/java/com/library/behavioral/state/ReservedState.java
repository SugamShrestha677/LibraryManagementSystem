package com.library.behavioral.state;

public class ReservedState implements BookState {
    @Override
    public void enter(BookStateContext context) {
        System.out.println("🔒 Book is RESERVED");
    }
    
    @Override
    public void handleBorrow(BookStateContext context) {
        System.out.println("✅ Borrowing reserved book...");
        context.setState(new BorrowedState());
    }
    
    @Override
    public void handleReturn(BookStateContext context) {
        System.out.println("⚠️ Book is reserved, cannot return.");
    }
    
    @Override
    public void handleReserve(BookStateContext context) {
        System.out.println("⚠️ Book is already reserved. Cannot reserve again.");
    }
    
    @Override
    public void handleCancelReservation(BookStateContext context) {
        System.out.println("✅ Cancelling reservation...");
        context.setState(new AvailableState());
    }
    
    @Override
    public String getStatus() {
        return "RESERVED";
    }
}