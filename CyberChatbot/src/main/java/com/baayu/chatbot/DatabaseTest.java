package com.baayu.chatbot;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DatabaseTest {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        try {
            db.connect();

            // Test User
            String username = "testuser_" + UUID.randomUUID().toString().substring(0, 8);
            User user = new User(0, username, "password123");
            db.createUser(user);
            User fetchedUser = db.getUserById(1);
            System.out.println("User: " + fetchedUser.getUsername());

            // Test Message
            Message message = new Message(0, 1, "Hello, world!", new Date(), false);
            db.createMessage(message);
            List<Message> messages = db.getMessagesByUser(1);
            for (Message msg : messages) {
                System.out.println("Message: " + msg.getContent());
            }

            // Test Session
            Session session = new Session(0, 1, UUID.randomUUID().toString(), new Date(), new Date(System.currentTimeMillis() + 3600000));
            db.createSession(session);
            Session fetchedSession = db.getSessionByToken(session.getSessionToken());
            System.out.println("Session Token: " + fetchedSession.getSessionToken());

            db.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
