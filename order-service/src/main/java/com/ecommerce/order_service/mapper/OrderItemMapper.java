package com.ecommerce.order_service.mapper;

import com.ecommerce.order_service.dto.request.ItemRequest;
import com.ecommerce.order_service.dto.request.OrderItemRequest;
import com.ecommerce.order_service.dto.response.ItemResponse;
import com.ecommerce.order_service.entity.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem toOrderItem(ItemRequest request);
    Set<OrderItem> toOrderItemSet(Set<ItemRequest> itemRequests);
    List<ItemResponse> toItemResponseList(List<OrderItem> itemList);
}