package com.ecommerce.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class NotificationEvent {
    String channel;
    String recipient;
    String templateCode;
    Map<String, Object> param;
    String subject;
    String body;
}