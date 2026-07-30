package com.library.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("❌ database.properties not found in classpath");
            }
            props.load(input);

            // Debug: print loaded properties (remove this after testing)
            System.out.println("✅ Loaded db.url: " + props.getProperty("db.url"));
            System.out.println("✅ Loaded db.user: " + props.getProperty("db.user"));
            // DO NOT print password in production, but for debugging you can check if it's loaded:
            System.out.println("✅ Password loaded: " + (props.getProperty("db.password") != null ? "Yes" : "No"));

            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        if (url == null || url.isEmpty()) {
            throw new SQLException("❌ db.url is missing or empty in database.properties");
        }
        if (user == null || user.isEmpty()) {
            throw new SQLException("❌ db.user is missing or empty in database.properties");
        }
        if (password == null) {
            throw new SQLException("❌ db.password is missing in database.properties");
        }

        return DriverManager.getConnection(url, user, password);
    }
}