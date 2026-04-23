package com.clinic.model;

import java.util.UUID;

public class Message {
    private String messageId;
    private String correlationId;
    private String type;
    private Object payload;
    private String replyTo;

    public Message() {
        this.messageId = "MSG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Message(String type, Object payload) {
        this();
        this.type = type;
        this.payload = payload;
    }

    public Message(String type, Object payload, String correlationId) {
        this();
        this.type = type;
        this.payload = payload;
        this.correlationId = correlationId;
    }

    // Getters and Setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Object getPayload() { return payload; }
    public void setPayload(Object payload) { this.payload = payload; }

    public String getReplyTo() { return replyTo; }
    public void setReplyTo(String replyTo) { this.replyTo = replyTo; }

    @Override
    public String toString() {
        return String.format("Message[id=%s, type=%s, correlation=%s]",
                messageId, type, correlationId);
    }
}
