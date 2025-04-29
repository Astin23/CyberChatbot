package com.baayu.chatbot;

import java.util.Date;

public class Session {
    private int sessionId;
    private int userId;
    private String sessionToken;
    private Date createdAt;
    private Date expiresAt;

    public Session(int sessionId, int userId, String sessionToken, Date createdAt, Date expiresAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.sessionToken = sessionToken;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; } // Added
    public int getUserId() { return userId; }
    public String getSessionToken() { return sessionToken; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getExpiresAt() { return expiresAt; }
}
