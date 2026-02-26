package com.ecommerce.inventory_service.controller;

import com.ecommerce.event.dto.ReleaseEvent;
import com.ecommerce.inventory_service.mapper.InventoryTransactionMapper;
import com.ecommerce.inventory_service.service.InventoryTransactionService;
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
public class kafkaConsumer {
    InventoryTransactionService transactionService;

    InventoryTransactionMapper transactionMapper;

    @KafkaListener(topics = "order-service.payment.transaction.error")
    public void listenerTransactionError(ReleaseEvent releaseEvents){
        transactionService.release(releaseEvents.getOrderId());

    }

    @KafkaListener(topics = "order-service.order.cancel")
    public void listenerOrderCancel(ReleaseEvent releaseEvents){
        transactionService.release(releaseEvents.getOrderId());

    }
}