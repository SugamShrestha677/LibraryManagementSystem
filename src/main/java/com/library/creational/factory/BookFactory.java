package com.library.creational.factory;

import com.library.model.Book;

public interface BookFactory {
    Book createBook(String bookId, String title, String author, String isbn);
}