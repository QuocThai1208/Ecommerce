package com.ecommerce.order_service.mapper;


import com.ecommerce.order_service.dto.request.ShipmentRequest;
import com.ecommerce.order_service.dto.response.ShipmentResponse;
import com.ecommerce.order_service.entity.OrderShipments;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    OrderShipments toShipment(ShipmentRequest request);
    ShipmentResponse toShipmentResponse(OrderShipments orderShipments);

    List<ShipmentResponse> toShipmentResponseList(List<OrderShipments> orderShipmentsList);
}