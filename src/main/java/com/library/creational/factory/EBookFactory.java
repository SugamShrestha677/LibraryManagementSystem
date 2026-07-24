package com.library.creational.factory;

import com.library.model.Book;
import com.library.model.EBook;

public class EBookFactory implements BookFactory {
    @Override
    public Book createBook(String bookId, String title, String author, String isbn) {
        return new EBook(bookId, title, author, isbn, "PDF", 5.2, "https://library.com/download/" + bookId);
    }
}