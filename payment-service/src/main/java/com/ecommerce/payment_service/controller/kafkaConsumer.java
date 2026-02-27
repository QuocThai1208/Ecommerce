package com.ecommerce.payment_service.controller;

import com.ecommerce.event.dto.*;

import com.ecommerce.payment_service.service.KafkaConsumerService;
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

    KafkaConsumerService kafkaConsumerService;


    @KafkaListener(topics = "order-service.order.payment.cancel")
    public void handleRefund(CancelEvent cancelEvent){
        kafkaConsumerService.handleRefund(cancelEvent);
    }

    @KafkaListener(topics = "order-service.order.cash-payment")
    public void listenOrderCashPayment(CashPaymentEvent cashPaymentEvent){
        kafkaConsumerService.createTransactionCash(cashPaymentEvent);
    }
}