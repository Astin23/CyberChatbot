package com.baayu.chatbot;

public class User {
    private int userId;
    private String username;
    private String passwordHash;

    public User(int userId, String username, String passwordHash) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; } // Added
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
