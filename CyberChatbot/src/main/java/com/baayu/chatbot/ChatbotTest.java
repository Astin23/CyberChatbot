package com.baayu.chatbot;

import java.sql.SQLException;

public class ChatbotTest {
    public static void main(String[] args) {
        try {
            Chatbot chatbot = new Chatbot();
            User user = chatbot.register("testuser_" + System.currentTimeMillis(), "password123");
            System.out.println("Registered user: " + user.getUsername());

            Session session = chatbot.login(user.getUsername(), "password123");
            if (session != null) {
                System.out.println("Logged in with session token: " + session.getSessionToken());
            }

            chatbot.sendMessage(user.getUserId(), "Hello from chatbot!", false);
            System.out.println("Message sent.");

            chatbot.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
