package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.request.ItemRequest;
import com.ecommerce.order_service.dto.request.ProductCheckoutRequest;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MapperService {
    public Set<ProductCheckoutRequest> toProductCheckoutRequests(Set<ItemRequest> items) {
        return items.stream()
                .map(item -> ProductCheckoutRequest.builder()
                        .productName(item.getProductNameSnapshot())
                        .quantity(item.getQuantity())
                        .amount(item.getUnitPriceSnapshot().longValue())
                        .build())
                .collect(Collectors.toSet());
    }
}