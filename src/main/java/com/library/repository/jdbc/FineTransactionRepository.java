package com.library.repository.jdbc;

import com.library.model.FineTransaction;
import com.library.repository.Repository;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FineTransactionRepository implements Repository<FineTransaction, String> {

    @Override
    public void save(FineTransaction fine) throws SQLException {
        String sql = "INSERT INTO fine_transactions (fine_id, user_id, amount, description, transaction_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fine.getFineId());
            stmt.setString(2, fine.getUserId());
            stmt.setDouble(3, fine.getAmount());
            stmt.setString(4, fine.getDescription());
            stmt.setDate(5, Date.valueOf(fine.getTransactionDate()));
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<FineTransaction> findById(String fineId) throws SQLException {
        String sql = "SELECT * FROM fine_transactions WHERE fine_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fineId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<FineTransaction> findAll() throws SQLException {
        List<FineTransaction> list = new ArrayList<>();
        String sql = "SELECT * FROM fine_transactions ORDER BY transaction_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public void update(FineTransaction fine) throws SQLException {
        String sql = "UPDATE fine_transactions SET user_id = ?, amount = ?, description = ?, transaction_date = ? " +
                     "WHERE fine_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fine.getUserId());
            stmt.setDouble(2, fine.getAmount());
            stmt.setString(3, fine.getDescription());
            stmt.setDate(4, Date.valueOf(fine.getTransactionDate()));
            stmt.setString(5, fine.getFineId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String fineId) throws SQLException {
        String sql = "DELETE FROM fine_transactions WHERE fine_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fineId);
            stmt.executeUpdate();
        }
    }

    // Additional helper: find by user ID
    public List<FineTransaction> findByUserId(String userId) throws SQLException {
        List<FineTransaction> list = new ArrayList<>();
        String sql = "SELECT * FROM fine_transactions WHERE user_id = ? ORDER BY transaction_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private FineTransaction mapRow(ResultSet rs) throws SQLException {
        return new FineTransaction(
            rs.getString("fine_id"),
            rs.getString("user_id"),
            rs.getDouble("amount"),
            rs.getString("description"),
            rs.getDate("transaction_date").toLocalDate()
        );
    }
}