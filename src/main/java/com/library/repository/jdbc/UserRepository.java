package com.library.repository.jdbc;

import com.library.model.User;
import com.library.repository.Repository;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<User, String> {

    @Override
    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (user_id, name, email, user_type, books_borrowed, outstanding_fine) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getUserType());
            stmt.setInt(5, user.getBooksBorrowed());
            stmt.setDouble(6, user.getOutstandingFine());
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<User> findById(String userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        return users;
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, user_type = ?, " +
                     "books_borrowed = ?, outstanding_fine = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getUserType());
            stmt.setInt(4, user.getBooksBorrowed());
            stmt.setDouble(5, user.getOutstandingFine());
            stmt.setString(6, user.getUserId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User(
            rs.getString("user_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("user_type")
        );
        user.setBooksBorrowed(rs.getInt("books_borrowed"));
        user.setOutstandingFine(rs.getDouble("outstanding_fine"));
        return user;
    }
}