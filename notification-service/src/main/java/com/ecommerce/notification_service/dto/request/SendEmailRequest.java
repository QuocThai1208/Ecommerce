package com.ecommerce.notification_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailRequest {
    Recipient to;
    String htmlContent;
    String subject;
}