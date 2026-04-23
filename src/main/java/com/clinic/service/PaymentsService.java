package com.clinic.service;

import com.clinic.config.BrokerConfig;
import com.clinic.dto.ProcessPaymentRequest;
import com.clinic.dto.ProcessPaymentResponse;
import com.clinic.model.Message;
import com.clinic.model.Payment;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PaymentsService {

    private final BrokerConfig broker;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Map<String, Payment> payments = new HashMap<>();

    public PaymentsService(BrokerConfig broker) {
        this.broker = broker;
    }

    @PostConstruct
    public void startConsumer() {
        executor.submit(() -> {
            System.out.println("[PaymentsService] Started listening for commands...");
            while (true) {
                try {
                    Message command = broker.receiveCommand("PAYMENTS");
                    handleCommand(command);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void handleCommand(Message command) throws InterruptedException {
        System.out.println("\n[PaymentsService] Received command: " + command.getType());

        switch (command.getType()) {
            case "PROCESS_PAYMENT" -> {
                ProcessPaymentRequest request = (ProcessPaymentRequest) command.getPayload();

                String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                Payment payment = new Payment(paymentId, request.getAppointmentId(),
                        request.getPatientId(), request.getAmount());
                payment.setStatus("COMPLETED");
                payment.setProcessedAt(LocalDateTime.now());
                payments.put(paymentId, payment);

                ProcessPaymentResponse response = new ProcessPaymentResponse(
                        paymentId, true, "Payment processed"
                );

                Message reply = new Message("PAYMENT_PROCESSED", response, command.getCorrelationId());
                broker.sendReply(reply);
            }

            case "REFUND_PAYMENT" -> {
                String paymentId = (String) command.getPayload();
                Payment payment = payments.get(paymentId);
                if (payment != null) {
                    payment.setStatus("REFUNDED");
                }

                Message reply = new Message("PAYMENT_REFUNDED", paymentId, command.getCorrelationId());
                broker.sendReply(reply);
            }
        }
    }
}
