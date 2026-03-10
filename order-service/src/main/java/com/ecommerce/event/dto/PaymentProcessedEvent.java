
package com.ecommerce.event.dto;

import com.ecommerce.order_service.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentProcessedEvent {
    String orderId;
    OrderStatus status;
}
