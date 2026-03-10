package com.ecommerce.order_service.service;

import com.ecommerce.event.dto.PaymentProcessedEvent;
import com.ecommerce.order_service.dto.request.PaymentCallbackRequest;
import com.ecommerce.order_service.dto.request.PaymentRequest;
import com.ecommerce.order_service.dto.response.PaymentResponse;
import com.ecommerce.order_service.entity.OrderPayment;
import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.enums.OrderStatus;
import com.ecommerce.order_service.enums.PaymentStatus;
import com.ecommerce.order_service.enums.TransactionType;
import com.ecommerce.order_service.exception.AppException;
import com.ecommerce.order_service.exception.ErrorCode;
import com.ecommerce.order_service.mapper.PaymentMapper;
import com.ecommerce.order_service.repository.OrdersRepository;
import com.ecommerce.order_service.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    static Set<PaymentStatus> FAILURE_STATUSES = Set.of(
            PaymentStatus.FAILED,
            PaymentStatus.REFUNDED,
            PaymentStatus.VOIDED
    );
    PaymentRepository paymentRepository;
    OrdersRepository ordersRepository;

    PaymentMapper paymentMapper;

    ApplicationEventPublisher eventPublisher;

    KafkaTemplate<String, Object> kafkaTemplate;

    public void createIntentPayment(Orders order, PaymentRequest request) {
        var payment = paymentMapper.toPayment(request);
        payment.setOrder(order);
        payment.setCreatedAt(Instant.now());
        paymentRepository.save(payment);
    }

    // Hoàn tiền đơn hàng
    public PaymentResponse refundPayment(String orderId){
        var order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        var parentPayment = paymentRepository.findByOrderIdAndStatusAndTransactionType(orderId, PaymentStatus.SUCCESS, TransactionType.PAYMENT)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        OrderPayment orderPayment = OrderPayment.builder()
                .order(order)
                .amount(order.getFinalAmount())
                .transactionType(TransactionType.REFUND)
                .status(PaymentStatus.PENDING)
                .originalTransactionId(parentPayment.getPaymentTransactionId())
                .paymentGateway(parentPayment.getPaymentGateway())
                .createdAt(Instant.now())
                .build();
        return paymentMapper.toResponse(paymentRepository.save(orderPayment));
    }

    // Api được gọi bởi payment service
    @Transactional
    public PaymentResponse paymentCallback(String orderId, PaymentCallbackRequest request){
        var payment = paymentRepository.findByOrderIdAndStatusAndTransactionType(orderId, PaymentStatus.PENDING, request.getTransactionType())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        paymentMapper.updatePayment(payment, request);

        OrderStatus status = OrderStatus.PENDING;
        if(request.getStatus() == PaymentStatus.SUCCESS){
            status = OrderStatus.PAID;
        }
        else if(FAILURE_STATUSES.contains(request.getStatus())){
            status = OrderStatus.CANCELLED;
        }
        // gủi sự kiện cho order service xử lý
        eventPublisher.publishEvent(PaymentProcessedEvent.builder()
                .orderId(orderId)
                .status(status)
                .build());

        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    public List<PaymentResponse> getPaymentByOrderId(String orderId){
        var payments = paymentRepository.findAllByOrderId(orderId);
        return paymentMapper.toPaymentResponses(payments);
    }

    // Huỷ đơn hàng bỏ giao dịch thanh toán
    public PaymentResponse cancelPayment(String orderId, String paymentTransactionId) {
        var payment = paymentRepository.findByOrderIdAndPaymentTransactionIdAndStatus(orderId, paymentTransactionId, PaymentStatus.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        payment.setStatus(PaymentStatus.VOIDED);
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }
}