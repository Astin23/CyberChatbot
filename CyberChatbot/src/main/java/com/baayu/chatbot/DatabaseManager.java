package com.baayu.chatbot;

import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseManager {
    private List<User> users = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<Session> sessions = new ArrayList<>();
    private int userIdCounter = 1;
    private int messageIdCounter = 1;
    private int sessionIdCounter = 1;

    public void createUser(User user) {
        user.setUserId(userIdCounter++);
        user.setPasswordHash(BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt()));
        users.add(user);
    }

    public User getUserById(int userId) {
        return users.stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public void createMessage(Message message) {
        message.setMessageId(messageIdCounter++);
        message.setTimestamp(new Date());
        messages.add(message);
    }

    public List<Message> getMessagesByUser(int userId) {
        return messages.stream()
                .filter(m -> m.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public void createSession(Session session) {
        session.setSessionId(sessionIdCounter++);
        session.setCreatedAt(new Date());
        sessions.add(session);
    }

    public Session getSessionByToken(String sessionToken) {
        return sessions.stream()
                .filter(s -> s.getSessionToken().equals(sessionToken))
                .findFirst()
                .orElse(null);
    }

    // Remove JDBC-specific methods
    public void connect() {
        // No-op for in-memory
    }

    public void close() {
        // No-op for in-memory
    }
}
