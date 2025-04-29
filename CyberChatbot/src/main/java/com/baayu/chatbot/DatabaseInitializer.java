package com.baayu.chatbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatbot.db")) {
            // Read init.sql from resources
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            DatabaseInitializer.class.getResourceAsStream("/init.sql")
                    )
            );
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
            reader.close();

            // Execute SQL
            Statement stmt = conn.createStatement();
            stmt.execute(sql.toString());
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
