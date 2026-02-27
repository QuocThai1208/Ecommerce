package com.ecommerce.payment_service.service;

import com.ecommerce.payment_service.dto.request.ProductCheckoutRequest;
import com.ecommerce.payment_service.dto.response.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Service
public class StripeService {
    @Value("${app.stripe.secret-key}")
    @NonFinal
    String secretKey;
    // request -> productName, amount, quantity, transactionId, orderId
    // response -> sessionId, sessionUrl
    public StripeResponse checkout(
            Set<ProductCheckoutRequest> requestList,
            String transactionId,
            List<String> orderIds){
        Stripe.apiKey = secretKey;

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        requestList.forEach(request -> {
            SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName(request.getProductName())
                    .build();

            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("usd")
                    .setUnitAmount(request.getAmount()/25000*100) // vnd -> usd -> cents
                    .setProductData(productData)
                    .build();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(priceData)
                    .setQuantity(request.getQuantity())
                    .build();
            lineItems.add(lineItem);
        });

        String joinedOrderIds = String.join(".", orderIds);
        SessionCreateParams params = SessionCreateParams.builder()
                .addAllLineItem(lineItems)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/success")
                .setCancelUrl("http://localhost:3000/cancel")
                .putMetadata("transaction_id", transactionId)
                .putMetadata("order_ids", joinedOrderIds)
                .build();

        Session session = null;
        try {
            session = Session.create(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return StripeResponse.builder()
                .sessionUrl(session.getUrl())
                .build();
    }

    public Refund refund(String paymentIntent, String orderId, BigDecimal amount){
        try{
            long stripeAmount = amount.longValue();
            log.info("paymentIntent: {}", paymentIntent);
            log.info("orderId: {}", orderId);
            log.info("amount: {}", amount);
            // Stripe sẽ chặn nếu trùng orderId trong 24h
            RequestOptions options = RequestOptions.builder()
                    .setIdempotencyKey(orderId)
                    .build();
            RefundCreateParams params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntent)
                    .setAmount(stripeAmount/25000*100)
                    .build();
            // gọi API refund từ stripe
            return Refund.create(params, options);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }
}