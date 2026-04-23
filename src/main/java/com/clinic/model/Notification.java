package com.clinic.model;

import java.time.LocalDateTime;

public class Notification {
    private String notificationId;
    private String recipientId;
    private String type;
    private String content;
    private String status;
    private LocalDateTime sentAt;

    public Notification() {}

    public Notification(String notificationId, String recipientId, String type, String content) {
        this.notificationId = notificationId;
        this.recipientId = recipientId;
        this.type = type;
        this.content = content;
        this.status = "PENDING";
    }

    public String getNotificationId() {
        return notificationId;
    }
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getRecipientId() {
        return recipientId;
    }
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return String.format("Notification[id=%s, recipient=%s, type=%s, status=%s]",
                notificationId, recipientId, type, status);
    }
}
