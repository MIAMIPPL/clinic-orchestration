package com.clinic.dto;

public class CreateAppointmentResponse {
    private String appointmentId;
    private boolean success;
    private String message;
    private double price;

    public CreateAppointmentResponse() {}

    public CreateAppointmentResponse(String appointmentId, boolean success, String message, double price) {
        this.appointmentId = appointmentId;
        this.success = success;
        this.message = message;
        this.price = price;
    }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}