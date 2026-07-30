package com.library.repository.jdbc;

import com.library.model.Book;
import com.library.model.PhysicalBook;
import com.library.model.EBook;
import com.library.repository.Repository;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository implements Repository<Book, String> {

    @Override
    public void save(Book book) throws SQLException {
        String sql = "INSERT INTO books (book_id, title, author, isbn, book_type, copies_available, base_cost, " +
                     "shelf_location, condition, file_format, file_size, download_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getBookId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getIsbn());
            stmt.setString(5, book.getBookType());
            stmt.setInt(6, book.getCopiesAvailable());
            stmt.setDouble(7, book.getBaseCost());

            if (book instanceof PhysicalBook) {
                PhysicalBook pb = (PhysicalBook) book;
                stmt.setString(8, pb.getShelfLocation());
                stmt.setString(9, pb.getCondition());
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.DECIMAL);
                stmt.setNull(12, Types.VARCHAR);
            } else if (book instanceof EBook) {
                EBook eb = (EBook) book;
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setString(10, eb.getFileFormat());
                stmt.setDouble(11, eb.getFileSize());
                stmt.setString(12, eb.getDownloadUrl());
            } else {
                throw new IllegalArgumentException("Unknown book type");
            }
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Book> findById(String bookId) throws SQLException {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY book_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        }
        return books;
    }

    @Override
    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, book_type = ?, " +
                     "copies_available = ?, base_cost = ?, shelf_location = ?, condition = ?, " +
                     "file_format = ?, file_size = ?, download_url = ? WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getBookType());
            stmt.setInt(5, book.getCopiesAvailable());
            stmt.setDouble(6, book.getBaseCost());

            if (book instanceof PhysicalBook) {
                PhysicalBook pb = (PhysicalBook) book;
                stmt.setString(7, pb.getShelfLocation());
                stmt.setString(8, pb.getCondition());
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.DECIMAL);
                stmt.setNull(11, Types.VARCHAR);
            } else if (book instanceof EBook) {
                EBook eb = (EBook) book;
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setString(9, eb.getFileFormat());
                stmt.setDouble(10, eb.getFileSize());
                stmt.setString(11, eb.getDownloadUrl());
            } else {
                throw new IllegalArgumentException("Unknown book type");
            }
            stmt.setString(12, book.getBookId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookId);
            stmt.executeUpdate();
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        String bookType = rs.getString("book_type");
        if ("Physical".equals(bookType)) {
            return new PhysicalBook(
                rs.getString("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getInt("copies_available"),
                rs.getString("shelf_location"),
                rs.getString("condition")
            );
        } else { // E-Book
            return new EBook(
                rs.getString("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getString("file_format"),
                rs.getDouble("file_size"),
                rs.getString("download_url")
            );
        }
    }
}