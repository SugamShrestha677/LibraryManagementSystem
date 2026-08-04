package com.library.repository.jdbc;

import com.library.model.Transaction;
import com.library.repository.Repository;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionRepository implements Repository<Transaction, String> {

    @Override
    public void save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (transaction_id, member_id, book_id, type, due_date, " +
                     "actual_return_date, fine_amount, is_active, priority, insurance) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transaction.getTransactionId());
            stmt.setString(2, transaction.getMemberId());
            stmt.setString(3, transaction.getBookId());
            stmt.setString(4, transaction.getType());
            stmt.setDate(5, Date.valueOf(transaction.getDueDate()));
            stmt.setDate(6, transaction.getActualReturnDate() != null ?
                         Date.valueOf(transaction.getActualReturnDate()) : null);
            stmt.setDouble(7, transaction.getFineAmount());
            stmt.setBoolean(8, transaction.isActive());
            stmt.setBoolean(9, transaction.isPriority());
            stmt.setBoolean(10, transaction.isInsurance());
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Transaction> findById(String transactionId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY transaction_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transactions.add(mapRow(rs));
            }
        }
        return transactions;
    }

    @Override
    public void update(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET member_id = ?, book_id = ?, type = ?, due_date = ?, " +
                     "actual_return_date = ?, fine_amount = ?, is_active = ?, priority = ?, insurance = ? " +
                     "WHERE transaction_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transaction.getMemberId());
            stmt.setString(2, transaction.getBookId());
            stmt.setString(3, transaction.getType());
            stmt.setDate(4, Date.valueOf(transaction.getDueDate()));
            stmt.setDate(5, transaction.getActualReturnDate() != null ?
                         Date.valueOf(transaction.getActualReturnDate()) : null);
            stmt.setDouble(6, transaction.getFineAmount());
            stmt.setBoolean(7, transaction.isActive());
            stmt.setBoolean(8, transaction.isPriority());
            stmt.setBoolean(9, transaction.isInsurance());
            stmt.setString(10, transaction.getTransactionId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transactionId);
            stmt.executeUpdate();
        }
    }

    public List<Transaction> findByMemberId(String userId) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE member_id = ? ORDER BY created_at DESC";
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

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction t = new Transaction(
            rs.getString("transaction_id"),
            rs.getString("member_id"),
            rs.getString("book_id"),
            rs.getString("type")
        );
        t.setDueDate(rs.getDate("due_date").toString());
        Date returnDate = rs.getDate("actual_return_date");
        if (returnDate != null) {
            t.setActualReturnDate(returnDate.toString());
        }
        t.setFineAmount(rs.getDouble("fine_amount"));
        t.setActive(rs.getBoolean("is_active"));
        t.setPriority(rs.getBoolean("priority"));
        t.setInsurance(rs.getBoolean("insurance"));
        return t;
    }
    
 // In TransactionRepository.java - add this method

    public int getMaxTransactionNumber() throws SQLException {
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(transaction_id, 4) AS INTEGER)), 0) FROM transactions";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
}