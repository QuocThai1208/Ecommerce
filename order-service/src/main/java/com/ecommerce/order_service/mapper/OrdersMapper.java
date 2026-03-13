package com.ecommerce.order_service.mapper;

import com.ecommerce.order_service.dto.request.*;
import com.ecommerce.order_service.dto.response.*;
import com.ecommerce.order_service.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    Orders toOrders(OrderCreationRequest request);

    OrderCreationResponse toOrderCreationResponse(Orders orders);

    OrderDetailResponse toOrderDetailResponse(Orders orders);

    OrderSummaryResponse toOrdersResponse(Orders orders);
    List<OrderSummaryResponse> toOrdersResponseList(List<Orders> ordersList);

    OrderStatusResponse toOrderStatusResponse(Orders orders);

    OrderReviewResponse toOrderReviewResponse(Orders order);

    @Mapping(target = "productVariantId", ignore = true)
    ItemRequest toItemRequest(OrderItemRequest orderItemRequest);

    Set<OrderItemRequest> toOrderItemRequests(Set<ItemRequest> itemRequests);

    Set<ItemRequest> toItemRequestSet(Set<ItemBatchDetailResponse> itemBatchDetailResponses);

    WarehouseBestRequest toWarehouseBestRequest(OrderReviewRequest request);

    ProductCheckout toProductCheckout(ProductAssignment assignment);
    Set<ProductCheckout> toProductCheckoutSet(Set<ProductAssignment> assignments);

    PurchaseResponse toPurchaseResponse(Orders orders);
    List<PurchaseResponse> toPurchaseResponses(List<Orders> orders);

}