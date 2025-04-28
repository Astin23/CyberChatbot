package com.baayu.chatbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteTest {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            System.out.println("SQLite connection successful!");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

