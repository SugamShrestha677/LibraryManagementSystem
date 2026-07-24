package com.library.behavioral.state;

public interface BookState {
    void enter(BookStateContext context);
    void handleBorrow(BookStateContext context);
    void handleReturn(BookStateContext context);
    void handleReserve(BookStateContext context);
    void handleCancelReservation(BookStateContext context);
    String getStatus();
}