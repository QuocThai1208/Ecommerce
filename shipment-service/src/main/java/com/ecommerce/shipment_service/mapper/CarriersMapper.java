package com.ecommerce.shipment_service.mapper;

import com.ecommerce.shipment_service.dto.request.CarrierCreationRequest;
import com.ecommerce.shipment_service.dto.response.CarrierResponse;
import com.ecommerce.shipment_service.entity.Carriers;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarriersMapper {
    Carriers toCarriers(CarrierCreationRequest request);
    CarrierResponse toCarrierResponse(Carriers carriers);
}