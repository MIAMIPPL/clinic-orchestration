package com.clinic.dto;

public class ProcessPaymentResponse {
    private String paymentId;
    private boolean success;
    private String message;

    public ProcessPaymentResponse() {}

    public ProcessPaymentResponse(String paymentId, boolean success, String message) {
        this.paymentId = paymentId;
        this.success = success;
        this.message = message;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
