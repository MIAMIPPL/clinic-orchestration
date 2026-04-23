package com.clinic.dto;

public class SendNotificationRequest {
    private String recipientId;
    private String type;
    private String content;

    public SendNotificationRequest() {}

    public SendNotificationRequest(String recipientId, String type, String content) {
        this.recipientId = recipientId;
        this.type = type;
        this.content = content;
    }

    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
