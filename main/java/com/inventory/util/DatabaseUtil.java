package com.inventory.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }

    public static void initializeDatabase() {
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS inventory_db";
        String createUsersTableSQL = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(50) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """;

        String createProductsTableSQL = """
                    CREATE TABLE IF NOT EXISTS products (
                        id VARCHAR(50) PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        price DOUBLE NOT NULL,
                        quantity INT NOT NULL,
                        user_id INT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
                    )
                """;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", USER, PASSWORD)) {
            // Create database
            conn.createStatement().execute(createDatabaseSQL);

            // Connect to the created database
            try (Connection dbConn = getConnection()) {
                // Create users table
                dbConn.createStatement().execute(createUsersTableSQL);
                // Create products table
                dbConn.createStatement().execute(createProductsTableSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}