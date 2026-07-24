package com.library.structural.proxy;

import java.util.List;

public interface LibraryService {
    boolean deleteBook(String bookId);
    List<String> viewAllBooks();
    List<String> viewAllUsers();
    boolean generateReport(String reportType);
}