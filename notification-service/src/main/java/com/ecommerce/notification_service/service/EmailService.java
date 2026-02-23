package com.ecommerce.notification_service.service;

import com.ecommerce.notification_service.dto.request.EmailRequest;
import com.ecommerce.notification_service.dto.request.SendEmailRequest;
import com.ecommerce.notification_service.dto.request.Sender;
import com.ecommerce.notification_service.dto.response.EmailResponse;
import com.ecommerce.notification_service.expention.AppException;
import com.ecommerce.notification_service.expention.ErrorCode;
import com.ecommerce.notification_service.repository.httpClient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    @NonFinal
    @Value("${notification.email.brevo-apiKey}")
    String apiKey;

    public EmailResponse sendEmail(SendEmailRequest request){
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Quốc Thái")
                        .email("thaiqp124@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .htmlContent(request.getHtmlContent())
                .subject(request.getSubject())
                .build();
        log.info("emailRequest: {}", emailRequest);

        try{
            return emailClient.sendEmail(apiKey, emailRequest);
        }catch(FeignException e){
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}