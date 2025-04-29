package com.baayu.chatbot;

import java.sql.SQLException;
import java.sql.PreparedStatement; // Added import
import java.sql.ResultSet; // Added import
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

public class Chatbot {
    private DatabaseManager db;

    public Chatbot() {
        db = new DatabaseManager();
        try {
            db.connect();
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    public User register(String username, String password) throws SQLException {
        User user = new User(0, username, password);
        db.createUser(user);
        return user;
    }

    public Session login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"));
                if (BCrypt.checkpw(password, user.getPasswordHash())) {
                    Session session = new Session(0, user.getUserId(), UUID.randomUUID().toString(),
                            new Date(), new Date(System.currentTimeMillis() + 3600000));
                    db.createSession(session);
                    return session;
                }
            }
            return null;
        }
    }

    public void sendMessage(int userId, String content, boolean encrypt) throws SQLException, Exception {
        if (encrypt) {
            content = EncryptionUtil.encrypt(content);
        }
        Message message = new Message(0, userId, content, new Date(), encrypt);
        db.createMessage(message);
    }

    public String getMessageContent(Message message) throws Exception {
        if (message.isEncrypted()) {
            return EncryptionUtil.decrypt(message.getContent());
        }
        return message.getContent();
    }

    public List<Message> getMessagesByUser(int userId) throws SQLException {
        return db.getMessagesByUser(userId);
    }

    public void close() throws SQLException {
        db.close();
    }
}
