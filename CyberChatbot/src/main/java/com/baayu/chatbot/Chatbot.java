package com.baayu.chatbot;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt; // Added import

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
        User user = db.getUserById(1); // Simplified for now, add proper lookup later
        if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {
            Session session = new Session(0, user.getUserId(), UUID.randomUUID().toString(),
                    new Date(), new Date(System.currentTimeMillis() + 3600000));
            db.createSession(session);
            return session;
        }
        return null;
    }

    public void sendMessage(int userId, String content, boolean encrypt) throws SQLException {
        Message message = new Message(0, userId, content, new Date(), encrypt);
        db.createMessage(message);
    }

    public void close() throws SQLException {
        db.close();
    }
}
