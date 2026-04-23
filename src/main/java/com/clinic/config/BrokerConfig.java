package com.clinic.config;

import com.clinic.model.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class BrokerConfig {

    // Очереди команд (от оркестратора к сервисам)
    private final BlockingQueue<Message> consumerCommandQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> appointmentsCommandQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> paymentsCommandQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> notificationsCommandQueue = new LinkedBlockingQueue<>();

    // Очередь ответов (от сервисов к оркестратору)
    private final BlockingQueue<Message> orchestratorReplyQueue = new LinkedBlockingQueue<>();

    // Корреляция запросов-ответов
    private final Map<String, Object> correlationData = new HashMap<>();

    public BlockingQueue<Message> getConsumerCommandQueue() {
        return consumerCommandQueue;
    }

    public BlockingQueue<Message> getAppointmentsCommandQueue() {
        return appointmentsCommandQueue;
    }

    public BlockingQueue<Message> getPaymentsCommandQueue() {
        return paymentsCommandQueue;
    }

    public BlockingQueue<Message> getNotificationsCommandQueue() {
        return notificationsCommandQueue;
    }

    public BlockingQueue<Message> getOrchestratorReplyQueue() {
        return orchestratorReplyQueue;
    }

    public void sendCommand(String queueType, Message message) throws InterruptedException {
        System.out.println("[BROKER] Sending command to " + queueType + ": " + message.getType());
        switch (queueType) {
            case "CONSUMER" -> consumerCommandQueue.put(message);
            case "APPOINTMENTS" -> appointmentsCommandQueue.put(message);
            case "PAYMENTS" -> paymentsCommandQueue.put(message);
            case "NOTIFICATIONS" -> notificationsCommandQueue.put(message);
        }
    }

    public void sendReply(Message message) throws InterruptedException {
        System.out.println("[BROKER] Sending reply: " + message.getType());
        orchestratorReplyQueue.put(message);
    }

    public Message receiveCommand(String queueType) throws InterruptedException {
        return switch (queueType) {
            case "CONSUMER" -> consumerCommandQueue.take();
            case "APPOINTMENTS" -> appointmentsCommandQueue.take();
            case "PAYMENTS" -> paymentsCommandQueue.take();
            case "NOTIFICATIONS" -> notificationsCommandQueue.take();
            default -> null;
        };
    }

    public Message receiveReply() throws InterruptedException {
        return orchestratorReplyQueue.take();
    }

    public void storeCorrelation(String correlationId, Object data) {
        correlationData.put(correlationId, data);
    }

    public Object getCorrelation(String correlationId) {
        return correlationData.get(correlationId);
    }
}
