package com.clinic.dto;

public class ProcessPaymentRequest {
    private String appointmentId;
    private String patientId;
    private double amount;

    public ProcessPaymentRequest() {}

    public ProcessPaymentRequest(String appointmentId, String patientId, double amount) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.amount = amount;
    }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}