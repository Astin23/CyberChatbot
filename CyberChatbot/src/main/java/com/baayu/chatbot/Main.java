package com.baayu.chatbot;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Chatbot chatbot = new Chatbot();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Welcome to CyberChatbot!");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Register user with error handling
            User user = null;
            try {
                user = chatbot.register(username, password);
                System.out.println("Registered: " + user.getUsername());
            } catch (Exception e) {
                System.out.println("Registration failed: " + e.getMessage());
                return;
            }

            // Login user with error handling
            Session session = chatbot.login(username, password);
            if (session != null) {
                System.out.println("Logged in! Session token: " + session.getSessionToken());
            } else {
                System.out.println("Login failed! Incorrect username or password.");
                return;
            }

            System.out.print("Enter message (type 'exit' to quit, 'encrypt' to toggle encryption, 'logout' to log out, 'history' to see messages): ");
            boolean encrypt = false;
            String message = scanner.nextLine();
            while (!message.equalsIgnoreCase("exit")) {
                if (message.equalsIgnoreCase("encrypt")) {
                    encrypt = !encrypt;
                    System.out.println("Encryption " + (encrypt ? "enabled" : "disabled"));
                } else if (message.equalsIgnoreCase("logout")) {
                    chatbot.logout(session.getSessionToken());
                    System.out.println("Logged out successfully.");
                    break;
                } else if (message.equalsIgnoreCase("history")) {
                    System.out.println("Your messages:");
                    for (Message msg : chatbot.getMessagesByUser(user.getUserId())) {
                        String content = chatbot.getMessageContent(msg);
                        System.out.println(" - " + content + (msg.isEncrypted() ? " (encrypted)" : ""));
                    }
                } else {
                    chatbot.sendMessage(user.getUserId(), message, encrypt);
                    System.out.println("Message sent: " + message + (encrypt ? " (encrypted)" : ""));
                }
                System.out.print("Enter message (type 'exit' to quit, 'encrypt' to toggle encryption, 'logout' to log out, 'history' to see messages): ");
                message = scanner.nextLine();
            }

            // Display all messages on exit
            System.out.println("Your messages:");
            for (Message msg : chatbot.getMessagesByUser(user.getUserId())) {
                String content = chatbot.getMessageContent(msg);
                System.out.println(" - " + content + (msg.isEncrypted() ? " (encrypted)" : ""));
            }

            chatbot.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

