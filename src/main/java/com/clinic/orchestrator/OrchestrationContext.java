package com.clinic.orchestrator;

import java.time.LocalDateTime;

public class OrchestrationContext {
    private String orchestrationId;
    private String patientId;
    private String doctorId;
    private String serviceType;
    private LocalDateTime dateTime;
    private String appointmentId;
    private String paymentId;
    private String notificationId;
    private OrchestrationState currentState;
    private String errorMessage;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private double amount;

    public OrchestrationContext(String patientId, String doctorId,
                                String serviceType, LocalDateTime dateTime) {
        this.orchestrationId = "ORC-" + System.currentTimeMillis();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.serviceType = serviceType;
        this.dateTime = dateTime;
        this.currentState = OrchestrationState.START;
        this.startedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getOrchestrationId() { return orchestrationId; }
    public void setOrchestrationId(String orchestrationId) { this.orchestrationId = orchestrationId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }

    public OrchestrationState getCurrentState() { return currentState; }
    public void setCurrentState(OrchestrationState currentState) { this.currentState = currentState; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    @Override
    public String toString() {
        return String.format("OrchestrationContext[id=%s, patient=%s, state=%s]",
                orchestrationId, patientId, currentState);
    }
}
