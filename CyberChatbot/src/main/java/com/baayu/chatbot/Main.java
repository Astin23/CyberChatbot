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

            User user = chatbot.register(username, password);
            System.out.println("Registered: " + user.getUsername());

            Session session = chatbot.login(username, password);
            if (session != null) {
                System.out.println("Logged in! Session token: " + session.getSessionToken());
            } else {
                System.out.println("Login failed!");
                return;
            }

            System.out.print("Enter message (type 'exit' to quit, 'encrypt' to toggle encryption): ");
            boolean encrypt = false;
            String message = scanner.nextLine();
            while (!message.equalsIgnoreCase("exit")) {
                if (message.equalsIgnoreCase("encrypt")) {
                    encrypt = !encrypt;
                    System.out.println("Encryption " + (encrypt ? "enabled" : "disabled"));
                } else {
                    chatbot.sendMessage(user.getUserId(), message, encrypt);
                    System.out.println("Message sent: " + message + (encrypt ? " (encrypted)" : ""));
                }
                System.out.print("Enter message (type 'exit' to quit, 'encrypt' to toggle encryption): ");
                message = scanner.nextLine();
            }

            // Display all messages
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
