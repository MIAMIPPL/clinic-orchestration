package com.clinic.dto;

public class VerifyConsumerResponse {
    private boolean verified;
    private String message;
    private String patientId;

    public VerifyConsumerResponse() {}

    public VerifyConsumerResponse(boolean verified, String message, String patientId) {
        this.verified = verified;
        this.message = message;
        this.patientId = patientId;
    }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
}
