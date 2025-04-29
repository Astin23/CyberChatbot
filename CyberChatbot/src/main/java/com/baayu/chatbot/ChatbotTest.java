package com.baayu.chatbot;

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

            chatbot.sendMessage(user.getUserId(), "Hello from chatbot!", true);
            System.out.println("Encrypted message sent.");

            // Fetch and decrypt message using Chatbot's public method
            for (Message msg : chatbot.getMessagesByUser(user.getUserId())) {
                String content = chatbot.getMessageContent(msg);
                System.out.println("Message: " + content);
            }

            chatbot.close();
        } catch (Exception e) { // Simplified catch block
            System.err.println("Error: " + e.getMessage());
        }
    }
}
