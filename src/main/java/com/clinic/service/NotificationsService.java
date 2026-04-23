package com.clinic.service;

import com.clinic.config.BrokerConfig;
import com.clinic.dto.SendNotificationRequest;
import com.clinic.dto.SendNotificationResponse;
import com.clinic.model.Message;
import com.clinic.model.Notification;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NotificationsService {

    private final BrokerConfig broker;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Map<String, Notification> notifications = new HashMap<>();

    public NotificationsService(BrokerConfig broker) {
        this.broker = broker;
    }

    @PostConstruct
    public void startConsumer() {
        executor.submit(() -> {
            System.out.println("[NotificationsService] Started listening for commands...");
            while (true) {
                try {
                    Message command = broker.receiveCommand("NOTIFICATIONS");
                    handleCommand(command);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void handleCommand(Message command) throws InterruptedException {
        System.out.println("\n[NotificationsService] Received command: " + command.getType());

        switch (command.getType()) {
            case "SEND_NOTIFICATION" -> {
                SendNotificationRequest request = (SendNotificationRequest) command.getPayload();

                String notificationId = "NOT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                Notification notification = new Notification(notificationId, request.getRecipientId(),
                        request.getType(), request.getContent());
                notification.setStatus("SENT");
                notification.setSentAt(LocalDateTime.now());
                notifications.put(notificationId, notification);

                SendNotificationResponse response = new SendNotificationResponse(
                        notificationId, true, "Notification sent"
                );

                Message reply = new Message("NOTIFICATION_SENT", response, command.getCorrelationId());
                broker.sendReply(reply);
            }
        }
    }
}