package com.clinic.dto;

public class VerifyConsumerRequest {
    private String patientId;

    public VerifyConsumerRequest() {}

    public VerifyConsumerRequest(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
}
