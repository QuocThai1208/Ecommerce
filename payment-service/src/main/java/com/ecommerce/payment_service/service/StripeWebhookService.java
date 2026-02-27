package com.ecommerce.payment_service.service;

import com.ecommerce.event.dto.CheckoutStatusEvent;
import com.ecommerce.event.dto.ReleaseEvent;
import com.ecommerce.payment_service.dto.request.TransactionUpdateRequest;
import com.ecommerce.payment_service.enums.PaymentStatus;
import com.ecommerce.payment_service.enums.TransactionType;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Service
public class StripeWebhookService {
    TransactionService transactionService;

    KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.stripe.webhook.secret}")
    @NonFinal
    String endpointSecret;

    public String handleStripeEvent(String payload, String sigHeader) {
        String result = "";
        try {
            //Xác thực event hợp lệ từ stripe
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            //Ép kiểu dữ liệu về session
            EventDataObjectDeserializer dataDeserializer = event.getDataObjectDeserializer();

            // Phân loại sự kiện
            switch (event.getType()) {
                case "checkout.session.completed":
                    if (dataDeserializer.getObject().isPresent()) {
                        // lấy Optional<StripeObject>
                        Session session = (Session) dataDeserializer.getObject().get();
                        String transactionId = session.getMetadata().get("transaction_id");

                        String joinedOrderIds = session.getMetadata().get("order_ids");
                        List<String> orderIds = new ArrayList<>();
                        if(joinedOrderIds != null && !joinedOrderIds.isEmpty()){
                            orderIds = Arrays.stream(joinedOrderIds.split(","))
                                    .map(String::trim)
                                    .toList();
                        }

                        String paymentIntentId = session.getPaymentIntent();
                        result = transactionService.updateStatusSuccess(TransactionUpdateRequest.builder()
                                .transactionId(transactionId)
                                .paymentIntentId(paymentIntentId)
                                .build());

                        orderIds.forEach(orderId -> {
                            kafkaTemplate.send("payment.transaction.status-change",
                                    CheckoutStatusEvent.builder()
                                            .orderId(orderId)
                                            .paymentTransactionId(transactionId)
                                            .transactionType(TransactionType.PAYMENT)
                                            .status(PaymentStatus.SUCCESS)
                                            .build());
                        });
                    }
                    break;
                case "checkout.session.async_payment_failed":
                    if (dataDeserializer.getObject().isPresent()) {
                        // lấy Optional<StripeObject>
                        Session session = (Session) dataDeserializer.getObject().get();
                        String transactionId = session.getMetadata().get("transaction_id");

                        String joinedOrderIds = session.getMetadata().get("order_ids");
                        List<String> orderIds = new ArrayList<>();
                        if(joinedOrderIds != null && !joinedOrderIds.isEmpty()){
                            orderIds = Arrays.stream(joinedOrderIds.split(","))
                                    .map(String::trim)
                                    .toList();
                        }

                        result = transactionService.updateStatusFailed(TransactionUpdateRequest.builder()
                                .transactionId(transactionId)
                                .build());

                        //Cập nhật lại trang thái đơn hàng
                        orderIds.forEach(orderId -> {
                            kafkaTemplate.send("payment.transaction.status-change",
                                    CheckoutStatusEvent.builder()
                                            .orderId(orderId)
                                            .paymentTransactionId(transactionId)
                                            .transactionType(TransactionType.PAYMENT)
                                            .status(PaymentStatus.SUCCESS)
                                            .build());
                            // hoàn lại số lượng sản phẩm vào kho
                            kafkaTemplate.send("order-service.order.cancel",
                                    ReleaseEvent.builder()
                                            .orderId(orderId)
                                            .build());
                        });
                        break;
                    }
                default:
                    log.info("Bỏ qua sự kiện: {}", event.getType());
            }

            return result;
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }
    }
}