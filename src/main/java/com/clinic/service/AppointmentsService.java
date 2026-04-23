package com.clinic.service;

import com.clinic.config.BrokerConfig;
import com.clinic.dto.CreateAppointmentRequest;
import com.clinic.dto.CreateAppointmentResponse;
import com.clinic.model.Appointment;
import com.clinic.model.Message;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AppointmentsService {

    private final BrokerConfig broker;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Map<String, Appointment> appointments = new HashMap<>();

    public AppointmentsService(BrokerConfig broker) {
        this.broker = broker;
    }

    @PostConstruct
    public void startConsumer() {
        executor.submit(() -> {
            System.out.println("[AppointmentsService] Started listening for commands...");
            while (true) {
                try {
                    Message command = broker.receiveCommand("APPOINTMENTS");
                    handleCommand(command);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void handleCommand(Message command) throws InterruptedException {
        System.out.println("\n[AppointmentsService] Received command: " + command.getType());

        switch (command.getType()) {
            case "CREATE_APPOINTMENT" -> {
                CreateAppointmentRequest request = (CreateAppointmentRequest) command.getPayload();

                String appointmentId = "APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                double price = calculatePrice(request.getServiceType());

                Appointment appointment = new Appointment(
                        appointmentId,
                        request.getPatientId(),
                        request.getDoctorId(),
                        request.getServiceType(),
                        request.getDateTime(),
                        price
                );

                appointments.put(appointmentId, appointment);

                CreateAppointmentResponse response = new CreateAppointmentResponse(
                        appointmentId, true, "Appointment created", price
                );

                Message reply = new Message("APPOINTMENT_CREATED", response, command.getCorrelationId());
                broker.sendReply(reply);
            }

            case "CANCEL_APPOINTMENT" -> {
                String appointmentId = (String) command.getPayload();
                Appointment appointment = appointments.get(appointmentId);
                if (appointment != null) {
                    appointment.setStatus("CANCELLED");
                }

                Message reply = new Message("APPOINTMENT_CANCELLED", appointmentId, command.getCorrelationId());
                broker.sendReply(reply);
            }
        }
    }

    private double calculatePrice(String serviceType) {
        return switch (serviceType.toLowerCase()) {
            case "consultation" -> 50.0;
            case "cleaning" -> 100.0;
            case "filling" -> 200.0;
            case "extraction" -> 150.0;
            default -> 100.0;
        };
    }
}
