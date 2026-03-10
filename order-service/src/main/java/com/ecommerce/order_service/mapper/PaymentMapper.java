package com.ecommerce.order_service.mapper;

import com.ecommerce.event.dto.CheckoutStatusEvent;
import com.ecommerce.order_service.dto.request.PaymentCallbackRequest;
import com.ecommerce.order_service.dto.request.PaymentRequest;
import com.ecommerce.order_service.dto.response.PaymentResponse;
import com.ecommerce.order_service.entity.OrderPayment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    OrderPayment toPayment(PaymentRequest request);
    void updatePayment(@MappingTarget OrderPayment payment, PaymentCallbackRequest request);

    PaymentCallbackRequest toPaymentCallbackRequest(CheckoutStatusEvent checkoutStatusEvent);

    PaymentResponse toResponse(OrderPayment payment);
    List<PaymentResponse> toPaymentResponses(List<OrderPayment> orderPayments);
}