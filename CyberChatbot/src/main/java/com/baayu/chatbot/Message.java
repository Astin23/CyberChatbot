package com.baayu.chatbot;

import java.util.Date;

public class Message {
    private int messageId;
    private int userId;
    private String content;
    private Date timestamp;
    private boolean isEncrypted;

    public Message(int messageId, int userId, String content, Date timestamp, boolean isEncrypted) {
        this.messageId = messageId;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
        this.isEncrypted = isEncrypted;
    }

    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; } // Added
    public int getUserId() { return userId; }
    public String getContent() { return content; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public boolean isEncrypted() { return isEncrypted; }
}

