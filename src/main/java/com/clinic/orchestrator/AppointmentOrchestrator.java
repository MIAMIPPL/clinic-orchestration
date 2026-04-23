package com.clinic.orchestrator;

import com.clinic.config.BrokerConfig;
import com.clinic.dto.*;
import com.clinic.model.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentOrchestrator {

    private final BrokerConfig broker;

    public AppointmentOrchestrator(BrokerConfig broker) {
        this.broker = broker;
    }

    public OrchestrationContext createAppointmentWithPayment(String patientId, String doctorId,
                                                             String serviceType, LocalDateTime dateTime) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ORCHESTRATOR: Starting appointment creation process");
        System.out.println("=".repeat(70));

        OrchestrationContext context = new OrchestrationContext(patientId, doctorId, serviceType, dateTime);
        String correlationId = context.getOrchestrationId();

        try {
            // Шаг 1: Проверка пациента (через брокер)
            context.setCurrentState(OrchestrationState.PENDING);
            System.out.println("\n[ORCHESTRATOR] State: PENDING");
            System.out.println("Action: Sending VERIFY_CONSUMER command...");

            VerifyConsumerRequest consumerRequest = new VerifyConsumerRequest(patientId);
            Message verifyMsg = new Message("VERIFY_CONSUMER", consumerRequest, correlationId);
            broker.sendCommand("CONSUMER", verifyMsg);

            // Ждем ответ
            Message reply = waitForReply(correlationId, "CONSUMER_VERIFIED");
            VerifyConsumerResponse consumerResponse = (VerifyConsumerResponse) reply.getPayload();

            if (!consumerResponse.isVerified()) {
                throw new Exception("Consumer verification failed: " + consumerResponse.getMessage());
            }

            context.setCurrentState(OrchestrationState.CONSUMER_VERIFIED);
            System.out.println("[ORCHESTRATOR] State: CONSUMER_VERIFIED");

            // Шаг 2: Создание записи
            context.setCurrentState(OrchestrationState.APPOINTMENT_CREATED);
            System.out.println("\n[ORCHESTRATOR] State: APPOINTMENT_CREATED");
            System.out.println("Action: Sending CREATE_APPOINTMENT command...");

            CreateAppointmentRequest apptRequest = new CreateAppointmentRequest(
                    patientId, doctorId, serviceType, dateTime
            );
            Message apptMsg = new Message("CREATE_APPOINTMENT", apptRequest, correlationId);
            broker.sendCommand("APPOINTMENTS", apptMsg);

            reply = waitForReply(correlationId, "APPOINTMENT_CREATED");
            CreateAppointmentResponse apptResponse = (CreateAppointmentResponse) reply.getPayload();

            context.setAppointmentId(apptResponse.getAppointmentId());
            context.setAmount(apptResponse.getPrice());

            // Шаг 3: Обработка платежа
            context.setCurrentState(OrchestrationState.PAYMENT_PROCESSED);
            System.out.println("\n[ORCHESTRATOR] State: PAYMENT_PROCESSED");
            System.out.println("Action: Sending PROCESS_PAYMENT command...");

            ProcessPaymentRequest paymentRequest = new ProcessPaymentRequest(
                    context.getAppointmentId(), patientId, context.getAmount()
            );
            Message paymentMsg = new Message("PROCESS_PAYMENT", paymentRequest, correlationId);
            broker.sendCommand("PAYMENTS", paymentMsg);

            reply = waitForReply(correlationId, "PAYMENT_PROCESSED");
            ProcessPaymentResponse paymentResponse = (ProcessPaymentResponse) reply.getPayload();
            context.setPaymentId(paymentResponse.getPaymentId());

            // Шаг 4: Отправка уведомления
            context.setCurrentState(OrchestrationState.NOTIFICATION_SENT);
            System.out.println("\n[ORCHESTRATOR] State: NOTIFICATION_SENT");
            System.out.println("Action: Sending SEND_NOTIFICATION command...");

            String content = String.format("Appointment confirmed! ID: %s, Service: %s, Date: %s",
                    context.getAppointmentId(), serviceType, dateTime);
            SendNotificationRequest notifRequest = new SendNotificationRequest(
                    patientId, "APPOINTMENT_CONFIRMATION", content
            );
            Message notifMsg = new Message("SEND_NOTIFICATION", notifRequest, correlationId);
            broker.sendCommand("NOTIFICATIONS", notifMsg);

            reply = waitForReply(correlationId, "NOTIFICATION_SENT");
            SendNotificationResponse notifResponse = (SendNotificationResponse) reply.getPayload();
            context.setNotificationId(notifResponse.getNotificationId());

            // Завершение
            context.setCurrentState(OrchestrationState.COMPLETED);
            context.setCompletedAt(LocalDateTime.now());
            System.out.println("\n[ORCHESTRATOR] State: COMPLETED");

        } catch (Exception e) {
            System.err.println("\n[ORCHESTRATOR] ERROR: " + e.getMessage());
            context.setErrorMessage(e.getMessage());
            compensate(context, correlationId);
        }

        printSummary(context);
        return context;
    }

    private Message waitForReply(String correlationId, String expectedType) throws InterruptedException {
        while (true) {
            Message reply = broker.receiveReply();
            if (reply.getCorrelationId().equals(correlationId) &&
                    reply.getType().equals(expectedType)) {
                return reply;
            }
            // Если не тот тип, сохраняем на потом
            broker.storeCorrelation(reply.getMessageId(), reply);
        }
    }

    private void compensate(OrchestrationContext context, String correlationId) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ORCHESTRATOR: Starting compensation process");
        System.out.println("=".repeat(70));

        try {
            if (context.getPaymentId() != null) {
                context.setCurrentState(OrchestrationState.COMPENSATING_PAYMENT);
                System.out.println("\n[ORCHESTRATOR] State: COMPENSATING_PAYMENT");
                Message refundMsg = new Message("REFUND_PAYMENT", context.getPaymentId(), correlationId);
                broker.sendCommand("PAYMENTS", refundMsg);
            }

            if (context.getAppointmentId() != null) {
                context.setCurrentState(OrchestrationState.COMPENSATING_APPOINTMENT);
                System.out.println("\n[ORCHESTRATOR] State: COMPENSATING_APPOINTMENT");
                Message cancelMsg = new Message("CANCEL_APPOINTMENT", context.getAppointmentId(), correlationId);
                broker.sendCommand("APPOINTMENTS", cancelMsg);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        context.setCurrentState(OrchestrationState.FAILED);
        context.setCompletedAt(LocalDateTime.now());
    }

    private void printSummary(OrchestrationContext context) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ORCHESTRATION SUMMARY");
        System.out.println("=".repeat(70));
        System.out.println("Orchestration ID: " + context.getOrchestrationId());
        System.out.println("Patient ID: " + context.getPatientId());
        System.out.println("Final State: " + context.getCurrentState());
        System.out.println("Appointment ID: " + context.getAppointmentId());
        System.out.println("Payment ID: " + context.getPaymentId());
        System.out.println("Notification ID: " + context.getNotificationId());
        System.out.println("Amount: $" + context.getAmount());
        System.out.println("=".repeat(70) + "\n");
    }
}
