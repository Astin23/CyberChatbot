package com.baayu.chatbot;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    public void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC"); // Explicitly register driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC Driver not found.", e);
        }
        connection = DriverManager.getConnection("jdbc:sqlite:chatbot.db");
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("SELECT 1 FROM sqlite_master WHERE type='table' AND name='users'");
        ResultSet rs = stmt.getResultSet();
        if (!rs.next()) {
            String sql = """
                CREATE TABLE users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL
                );
                CREATE TABLE messages (
                    message_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    content TEXT NOT NULL,
                    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                    is_encrypted BOOLEAN DEFAULT FALSE,
                    FOREIGN KEY (user_id) REFERENCES users(user_id)
                );
                CREATE TABLE sessions (
                    session_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    session_token TEXT NOT NULL UNIQUE,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    expires_at DATETIME NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(user_id)
                );
                """;
            stmt.execute(sql);
        }
        stmt.close();
    }

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt()));
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
            }
        }
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"));
            }
            return null;
        }
    }

    public void createMessage(Message message) throws SQLException {
        String sql = "INSERT INTO messages (user_id, content, timestamp, is_encrypted) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, message.getUserId());
            pstmt.setString(2, message.getContent());
            pstmt.setTimestamp(3, new Timestamp(message.getTimestamp().getTime()));
            pstmt.setBoolean(4, message.isEncrypted());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                message.setMessageId(rs.getInt(1));
            }
        }
    }

    public List<Message> getMessagesByUser(int userId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("user_id"),
                        rs.getString("content"),
                        rs.getTimestamp("timestamp"),
                        rs.getBoolean("is_encrypted")
                ));
            }
        }
        return messages;
    }

    public void createSession(Session session) throws SQLException {
        String sql = "INSERT INTO sessions (user_id, session_token, created_at, expires_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, session.getUserId());
            pstmt.setString(2, session.getSessionToken());
            pstmt.setTimestamp(3, new Timestamp(session.getCreatedAt().getTime()));
            pstmt.setTimestamp(4, new Timestamp(session.getExpiresAt().getTime()));
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                session.setSessionId(rs.getInt(1));
            }
        }
    }

    public Session getSessionByToken(String sessionToken) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE session_token = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, sessionToken);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Session(
                        rs.getInt("session_id"),
                        rs.getInt("user_id"),
                        rs.getString("session_token"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("expires_at")
                );
            }
            return null;
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

