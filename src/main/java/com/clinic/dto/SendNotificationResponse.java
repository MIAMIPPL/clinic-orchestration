package com.clinic.dto;

public class SendNotificationResponse {
    private String notificationId;
    private boolean success;
    private String message;

    public SendNotificationResponse() {}

    public SendNotificationResponse(String notificationId, boolean success, String message) {
        this.notificationId = notificationId;
        this.success = success;
        this.message = message;
    }

    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}