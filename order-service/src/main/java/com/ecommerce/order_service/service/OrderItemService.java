package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.request.ItemRequest;
import com.ecommerce.order_service.dto.response.ItemResponse;
import com.ecommerce.order_service.dto.response.PageResponse;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.mapper.OrderItemMapper;
import com.ecommerce.order_service.repository.OrderItemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemService {
    OrderItemRepository orderItemRepository;
    OrderItemMapper orderItemMapper;

    @Transactional
    public void createOrderItems(Orders order, Set<ItemRequest> itemRequests){
        Set<OrderItem> orderItemSet = orderItemMapper.toOrderItemSet(itemRequests);
        List<OrderItem> itemsToSave = new ArrayList<>();
        var now = Instant.now();
        orderItemSet.forEach(item -> {
            var quantity = item.getQuantity();
            var unitPrice = item.getUnitPriceSnapshot();
            BigDecimal totalItemAmount = BigDecimal.valueOf(quantity).multiply(unitPrice);
            item.setOrder(order);
            item.setTotalItemAmount(totalItemAmount);
            item.setCreatedAt(now);

            itemsToSave.add(item);
        });
        orderItemRepository.saveAll(itemsToSave);
    }

    // Lấy các item trong thuộc order
    public PageResponse<ItemResponse> getOrderItems(String orderId, int page, int size){
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = orderItemRepository.findAllByOrderId(orderId, pageable);
        var itemList = orderItemMapper.toItemResponseList(pageData.getContent());
        return PageResponse.<ItemResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(itemList)
                .build();
    }
}