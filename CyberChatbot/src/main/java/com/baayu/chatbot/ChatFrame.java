package com.baayu.chatbot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ChatFrame extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JCheckBox encryptCheckBox;
    private Chatbot chatbot;
    private Session currentSession;
    private String currentUsername;

    public ChatFrame(Chatbot chatbot, Session session, String username) {
        this.chatbot = chatbot;
        this.currentSession = session;
        this.currentUsername = username;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("CyberChatbot - Chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        mainPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        messageField = new JTextField(30);
        encryptCheckBox = new JCheckBox("Encrypt");
        JButton sendButton = new JButton("Send");
        JButton historyButton = new JButton("History");
        JButton logoutButton = new JButton("Logout");
        JButton deleteUserButton = new JButton("Delete User");

        inputPanel.add(messageField);
        inputPanel.add(encryptCheckBox);
        inputPanel.add(sendButton);
        inputPanel.add(historyButton);
        inputPanel.add(logoutButton);
        inputPanel.add(deleteUserButton);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if (!message.isEmpty()) {
                    try {
                        boolean isEncrypted = encryptCheckBox.isSelected();
                        chatbot.sendMessage(currentSession.getUserId(), message, isEncrypted);
                        chatArea.append("You: " + message + (isEncrypted ? " (encrypted)" : "") + "\n");
                        messageField.setText("");
                    } catch (Exception ex) {
                        chatArea.append("Error: " + ex.getMessage() + "\n");
                    }
                }
            }
        });

        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Message> messages = chatbot.getMessagesByUser(currentSession.getUserId());
                    chatArea.append("History:\n");
                    for (Message msg : messages) {
                        String content = chatbot.getMessageContent(msg);
                        chatArea.append(" - " + content + (msg.isEncrypted() ? " (encrypted)" : "") + "\n");
                    }
                } catch (Exception ex) {
                    chatArea.append("Error: " + ex.getMessage() + "\n");
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    chatbot.logout(currentSession.getSessionToken());
                    chatArea.append("Logged out successfully.\n");
                    dispose(); // Close chat frame
                    new LoginFrame().setVisible(true);
                } catch (SQLException ex) {
                    chatArea.append("Error: " + ex.getMessage() + "\n");
                }
            }
        });

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameToDelete = JOptionPane.showInputDialog("Enter username to delete:");
                if (usernameToDelete != null && !usernameToDelete.isEmpty()) {
                    try {
                        chatbot.deleteUser(usernameToDelete);
                        chatArea.append("User '" + usernameToDelete + "' deleted successfully.\n");
                    } catch (SQLException ex) {
                        chatArea.append("Error deleting user: " + ex.getMessage() + "\n");
                    }
                }
            }
        });

        setVisible(true);
        chatArea.append("Welcome, " + currentUsername + "! (Session: " + currentSession.getSessionToken() + ")\n");
    }

    public static void main(String[] args) {
        // Only for testing, normally called from LoginFrame
    }
}
