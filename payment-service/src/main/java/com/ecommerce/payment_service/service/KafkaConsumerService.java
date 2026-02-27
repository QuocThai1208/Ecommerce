package com.ecommerce.payment_service.service;

import com.ecommerce.event.dto.*;
import com.ecommerce.payment_service.entity.Refund;
import com.ecommerce.payment_service.enums.RefundStatus;
import com.ecommerce.payment_service.enums.TransactionStatus;
import com.ecommerce.payment_service.exception.AppException;
import com.ecommerce.payment_service.exception.ErrorCode;
import com.ecommerce.payment_service.mapper.TransactionMapper;
import com.ecommerce.payment_service.repository.DetailRepository;
import com.ecommerce.payment_service.repository.RefundRepository;
import com.ecommerce.payment_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Service
public class KafkaConsumerService {
    StripeService stripeService;

    TransactionRepository transactionRepository;
    DetailRepository detailRepository;
    RefundRepository refundRepository;

    TransactionMapper transactionMapper;

    KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void handleRefund(CancelEvent cancelEvent){
        try{
            var transactionDetail = detailRepository.findByOrderId(cancelEvent.getOrderId())
                    .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_DETAIL_NOT_FOUND));

            transactionDetail.setStatus(TransactionStatus.REFUNDED);

            var transaction = transactionDetail.getTransaction();

            String paymentIntentId = transaction.getPaymentIntentId();
            // Gọi api hủy của stripe
            var stripeRefund = stripeService.refund(paymentIntentId, cancelEvent.getOrderId(), transactionDetail.getAmount());

            var now = Instant.now();
            Refund refund = Refund.builder()
                    .transaction(transaction)
                    .refundAmount(transactionDetail.getAmount())
                    .status(RefundStatus.COMPLETED)
                    .reason(cancelEvent.getReason())
                    .gatewayRefundId(stripeRefund.getId())
                    .createdAt(now)
                    .updateAt(now)
                    .build();
            refundRepository.save(refund);

            // Cập nhật lại trạng thái order
            kafkaTemplate.send("payment-service.refund.status",
                    RefundEvent.builder()
                            .orderId(cancelEvent.getOrderId())
                            .status("COMPLETED")
                            .build());

            // hoàn lại số lượng sản phẩm vào kho
            kafkaTemplate.send("order-service.order.cancel",
                    ReleaseEvent.builder()
                            .orderId(cancelEvent.getOrderId())
                            .build());

            // Gửi thông báo qua email
            NotificationEvent notificationEvent = NotificationEvent.builder()
                    .channel("email")
                    .recipient(cancelEvent.getEmail())
                    .subject("Hủy thành công đơn hàng có mã số " + cancelEvent.getOrderId())
                    .body("Đã hoàn tiền cho đơn hàng " + cancelEvent.getOrderId() + ". Số tiền hoàn lại là " + transactionDetail.getAmount())
                    .build();
            kafkaTemplate.send("notification-delivery", notificationEvent);

        }catch (Exception e){
            //Cập nhật lại trạng thái order
            kafkaTemplate.send("payment-service.refund.status",
                    RefundEvent.builder()
                            .orderId(cancelEvent.getOrderId())
                            .status("FAILED")
                            .build());
            // Gửi thông báo qua email
            NotificationEvent notificationEvent = NotificationEvent.builder()
                    .channel("email")
                    .recipient(cancelEvent.getEmail())
                    .subject("Yêu cầu hủy đơn hàng gặp sự cố. Chúng tôi sẽ xử lý trong vòng 3-5 ngày tới.")
                    .body("Hủy đơn hàng " + cancelEvent.getOrderId() + " không thành công.")
                    .build();
            kafkaTemplate.send("notification-delivery", notificationEvent);
        }
    }


    public void createTransactionCash(CashPaymentEvent cashPaymentEvent) {
        String transactionId = UUID.randomUUID().toString();
        var transaction = transactionMapper.cashPaymentEventToTransaction(cashPaymentEvent);

        transaction.setId(transactionId);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(Instant.now());
        transaction.setUpdateAt(Instant.now());
        transactionRepository.save(transaction);
    }
}