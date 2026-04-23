package com.clinic.model;

import java.time.LocalDateTime;

public class Payment {
    private String paymentId;
    private String appointmentId;
    private String patientId;
    private double amount;
    private String status;
    private LocalDateTime processedAt;

    public Payment() {}

    public Payment(String paymentId, String appointmentId, String patientId, double amount) {
        this.paymentId = paymentId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.amount = amount;
        this.status = "PENDING";
    }

    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public String toString() {
        return String.format("Payment[id=%s, appointment=%s, amount=%.2f, status=%s]",
                paymentId, appointmentId, amount, status);
    }
}
