package com.library.creational.factory;

import com.library.model.Book;
import com.library.model.PhysicalBook;

public class PhysicalBookFactory implements BookFactory {
    @Override
    public Book createBook(String bookId, String title, String author, String isbn) {
        return new PhysicalBook(bookId, title, author, isbn, 3, "A-12", "NEW");
    }
}