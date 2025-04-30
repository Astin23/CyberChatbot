package com.baayu.chatbot;

import java.sql.SQLException;
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
            System.out.println("Creating user: " + user.getUsername() + " with initial ID: " + user.getUserId());
            db.createUser(user);
            System.out.println("Created user ID after insert: " + user.getUserId()); // Debug user ID

            // Fetch the user using the correct ID
            User fetchedUser = db.getUserById(user.getUserId()); // Use getUserById instead of getUser
            if (fetchedUser != null) {
                System.out.println("Fetched user: " + fetchedUser.getUsername());
            } else {
                System.out.println("Fetched user is null. User ID: " + user.getUserId() + " might be incorrect or user not found.");
            }

            // Test Message
            if (fetchedUser != null) {
                Message message = new Message(0, fetchedUser.getUserId(), "Hello, world!", new Date(), false);
                System.out.println("Creating message for user ID: " + fetchedUser.getUserId());
                db.createMessage(message);
                List<Message> messages = db.getMessagesByUser(fetchedUser.getUserId());
                System.out.println("Messages found: " + messages.size());
                for (Message msg : messages) {
                    System.out.println("Message: " + msg.getContent());
                }
            } else {
                System.out.println("Skipping message test as user fetch failed.");
            }

            // Test Session
            if (fetchedUser != null) {
                Session session = new Session(0, fetchedUser.getUserId(), UUID.randomUUID().toString(), new Date(), new Date(System.currentTimeMillis() + 3600000));
                System.out.println("Creating session for user ID: " + fetchedUser.getUserId());
                db.createSession(session);
                Session fetchedSession = db.getSessionByToken(session.getSessionToken());
                if (fetchedSession != null) {
                    System.out.println("Session Token: " + fetchedSession.getSessionToken());
                } else {
                    System.out.println("Fetched session is null. Token might be invalid.");
                }
            } else {
                System.out.println("Skipping session test as user fetch failed.");
            }

            db.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for detailed error
        }
    }
}

