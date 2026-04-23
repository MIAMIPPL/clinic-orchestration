package com.clinic.dto;

import java.time.LocalDateTime;

public class CreateAppointmentRequest {
    private String patientId;
    private String doctorId;
    private String serviceType;
    private LocalDateTime dateTime;

    public CreateAppointmentRequest() {}

    public CreateAppointmentRequest(String patientId, String doctorId, String serviceType, LocalDateTime dateTime) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.serviceType = serviceType;
        this.dateTime = dateTime;
    }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
}
