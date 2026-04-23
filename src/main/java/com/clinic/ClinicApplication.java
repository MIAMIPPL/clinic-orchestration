package com.clinic;

import com.clinic.orchestrator.AppointmentOrchestrator;
import com.clinic.orchestrator.OrchestrationContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class ClinicApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClinicApplication.class,args);
    }

    @Bean
    public CommandLineRunner demo(AppointmentOrchestrator orchestrator) {
        return args -> {
            // Тестовый сценарий 1: Успешное создание записи
            System.out.println("\n\n>>>SCENARIO 1: Successful appointment creation");
            OrchestrationContext context1 = orchestrator.createAppointmentWithPayment(
                    "PATIENT-001",
                    "DOCTOR-123",
                    "cleaning",
                    LocalDateTime.of(2026, 4, 15, 10, 0)
            );

            Thread.sleep(2000);

            System.out.println("\n\n>>>SCENARIO 2: Another successful appointment");
            OrchestrationContext context2 = orchestrator.createAppointmentWithPayment(
                    "PATIENT-002",
                    "DOCTOR-456",
                    "consultation",
                    LocalDateTime.of(2026, 4, 16, 14, 30)
            );

            Thread.sleep(2000);

            System.out.println("\n\n>>>SCENARIO 3: Failed appointment (consumer not verified)");
            OrchestrationContext context3 = orchestrator.createAppointmentWithPayment(
                    "PATIENT-999",  // Не существует
                    "DOCTOR-789",
                    "filling",
                    LocalDateTime.of(2026, 4, 17, 9, 0)
            );
        };
    }
}
