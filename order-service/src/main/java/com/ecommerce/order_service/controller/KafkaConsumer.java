package com.ecommerce.order_service.controller;


import com.ecommerce.event.dto.CheckoutStatusEvent;
import com.ecommerce.event.dto.RefundEvent;
import com.ecommerce.order_service.enums.OrderStatus;
import com.ecommerce.order_service.enums.PaymentStatus;
import com.ecommerce.order_service.enums.TransactionType;
import com.ecommerce.order_service.exception.AppException;
import com.ecommerce.order_service.exception.ErrorCode;
import com.ecommerce.order_service.mapper.PaymentMapper;
import com.ecommerce.order_service.repository.OrdersRepository;
import com.ecommerce.order_service.repository.PaymentRepository;
import com.ecommerce.order_service.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumer {
    PaymentService paymentService;

    OrdersRepository ordersRepository;
    PaymentRepository paymentRepository;

    PaymentMapper paymentMapper;

    @KafkaListener(topics = "payment.transaction.status-change")
    public void listenPaymentStatusChange(CheckoutStatusEvent checkoutStatusEvent){
        var paymentCallbackRequest = paymentMapper.toPaymentCallbackRequest(checkoutStatusEvent);
        paymentService.paymentCallback(checkoutStatusEvent.getOrderId(), paymentCallbackRequest);
    }

    @KafkaListener(topics = "payment-service.refund.status")
    @Transactional
    public void listenPaymentRefundStatus(RefundEvent refundEvent){
        var order = ordersRepository.findById(refundEvent.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        var payment = paymentRepository.findByOrderIdAndTransactionTypeAndStatus(order.getId(), TransactionType.REFUND, PaymentStatus.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        if(refundEvent.getStatus().equals("COMPLETED")){
            order.setStatus(OrderStatus.CANCELLED);
            payment.setStatus(PaymentStatus.REFUNDED);
        }else if(refundEvent.getStatus().equals("FAILED")){
            order.setStatus(OrderStatus.CANCEL_FAILURE);
            payment.setStatus(PaymentStatus.FAILED);
        }
        paymentRepository.save(payment);
        ordersRepository.save(order);
    }
}