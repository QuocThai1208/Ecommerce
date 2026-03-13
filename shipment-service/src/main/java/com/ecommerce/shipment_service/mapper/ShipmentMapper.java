package com.ecommerce.shipment_service.mapper;

import com.ecommerce.event.dto.ShipmentCreationEvent;
import com.ecommerce.shipment_service.dto.request.CalculatorFeeItemRequest;
import com.ecommerce.shipment_service.dto.request.CalculatorFeeRequest;
import com.ecommerce.shipment_service.dto.request.GhnFeeRequest;
import com.ecommerce.shipment_service.dto.response.ShipmentResponse;
import com.ecommerce.shipment_service.entity.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    Shipment toShipment(ShipmentCreationEvent request);

    @Mapping(target = "userAddress", ignore = true)
    ShipmentResponse toResponse(Shipment shipment);

    GhnFeeRequest toGhnFeeRequest(CalculatorFeeItemRequest request);
}