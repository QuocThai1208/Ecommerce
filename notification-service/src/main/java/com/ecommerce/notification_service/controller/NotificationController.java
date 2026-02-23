package com.ecommerce.notification_service.controller;

import com.ecommerce.event.dto.NotificationEvent;
import com.ecommerce.notification_service.dto.request.Recipient;
import com.ecommerce.notification_service.dto.request.SendEmailRequest;
import com.ecommerce.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailService emailService;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationDelivery(NotificationEvent notificationEvent){
        emailService.sendEmail(SendEmailRequest.builder()
                .to(Recipient.builder()
                        .email(notificationEvent.getRecipient())
                        .build())
                .htmlContent(notificationEvent.getBody())
                .subject(notificationEvent.getSubject())
                .build());
    }
}