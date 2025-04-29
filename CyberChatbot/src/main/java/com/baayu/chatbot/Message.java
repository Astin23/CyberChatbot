package com.baayu.chatbot;

import java.util.Date;

public class Message {
    private int messageId;
    private int userId;
    private String content;
    private Date timestamp;
    private boolean isEncrypted;

    // Constructor
    public Message(int messageId, int userId, String content, Date timestamp, boolean isEncrypted) {
        this.messageId = messageId;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
        this.isEncrypted = isEncrypted;
    }

    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }

    public int getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        isEncrypted = encrypted;
    }
}
