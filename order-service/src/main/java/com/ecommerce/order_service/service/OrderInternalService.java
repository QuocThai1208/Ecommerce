package com.ecommerce.order_service.service;


import com.ecommerce.order_service.dto.request.ItemRequest;
import com.ecommerce.order_service.dto.request.PaymentRequest;
import com.ecommerce.order_service.dto.request.ShippingAddressRequest;
import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.enums.PaymentStatus;
import com.ecommerce.order_service.enums.TransactionType;
import com.ecommerce.order_service.repository.OrdersRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderInternalService {
    OrdersRepository ordersRepository;

    PaymentService paymentService;
    OrderItemService orderItemService;
    ShippingAddressService shippingAddressService;

    @Transactional
    public List<Orders> saveMultipleOrder(
            List<Orders> orders,
            Map<String, Set<ItemRequest>> itemRequestsMap
    ) {

        var ordersSave = ordersRepository.saveAll(orders);
        ordersSave.forEach(order -> {
            // tạo payment
            paymentService.createIntentPayment(order, PaymentRequest.builder()
                    .amount(order.getFinalAmount())
                    .transactionType(TransactionType.PAYMENT)
                    .status(PaymentStatus.PENDING)
                    .paymentGateway("STRIPE")
                    .build());
            orderItemService.createOrderItems(order, itemRequestsMap.get(order.getId()));
        });
        return ordersSave;
    }
}