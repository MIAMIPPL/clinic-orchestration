package com.clinic.service;

import com.clinic.config.BrokerConfig;
import com.clinic.dto.VerifyConsumerRequest;
import com.clinic.dto.VerifyConsumerResponse;
import com.clinic.model.Message;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ConsumerService {

    private final BrokerConfig broker;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Map<String, Boolean> consumers = new HashMap<>();

    public ConsumerService(BrokerConfig broker) {
        this.broker = broker;
        // Инициализация тестовыми данными
        consumers.put("PATIENT-001", true);
        consumers.put("PATIENT-002", true);
        consumers.put("PATIENT-003", false);
    }

    @PostConstruct
    public void startConsumer() {
        executor.submit(() -> {
            System.out.println("[ConsumerService] Started listening for commands...");
            while (true) {
                try {
                    Message command = broker.receiveCommand("CONSUMER");
                    handleCommand(command);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void handleCommand(Message command) throws InterruptedException {
        System.out.println("\n[ConsumerService] Received command: " + command.getType());

        switch (command.getType()) {
            case "VERIFY_CONSUMER" -> {
                VerifyConsumerRequest request = (VerifyConsumerRequest) command.getPayload();
                boolean verified = consumers.getOrDefault(request.getPatientId(), false);

                VerifyConsumerResponse response = new VerifyConsumerResponse(
                        verified,
                        verified ? "Consumer verified" : "Consumer not found",
                        request.getPatientId()
                );

                Message reply = new Message(
                        verified ? "CONSUMER_VERIFIED" : "CONSUMER_VERIFICATION_FAILED",
                        response,
                        command.getCorrelationId()
                );

                broker.sendReply(reply);
            }
        }
    }
}
