package com.baayu.chatbot;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {
    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:chatbot.db");
    }

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt()));
            pstmt.executeUpdate();
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
        String sql = "INSERT INTO sessions (user_id, session_token, expires_at) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, session.getUserId());
            pstmt.setString(2, session.getSessionToken());
            pstmt.setTimestamp(3, new Timestamp(session.getExpiresAt().getTime()));
            pstmt.executeUpdate();
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
