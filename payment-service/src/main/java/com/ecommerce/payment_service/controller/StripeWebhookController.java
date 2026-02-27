package com.ecommerce.payment_service.controller;

import com.ecommerce.payment_service.dto.ApiResponse;
import com.ecommerce.payment_service.service.StripeWebhookService;
import com.ecommerce.payment_service.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/webhooks")
public class StripeWebhookController {
    TransactionService transactionService;
    StripeWebhookService stripeWebhookService;


    @Value("${app.stripe.webhook.secret}")
    @NonFinal
    String endpointSecret;

    @PostMapping("/stripe")
    ApiResponse<String> handleStripeEvent(
            @RequestBody String payload,
            // lấy signature của stripe trong header
            @RequestHeader("Stripe-Signature") String sigHeader) {

        return ApiResponse.<String>builder()
                .message("Cập nhật trạng thái thành công.")
                .result(stripeWebhookService.handleStripeEvent(payload, sigHeader))
                .build();
    }
}