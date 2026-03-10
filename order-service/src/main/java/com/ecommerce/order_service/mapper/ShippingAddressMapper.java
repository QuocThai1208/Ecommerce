package com.ecommerce.order_service.mapper;

import com.ecommerce.order_service.dto.request.ShippingAddressRequest;
import com.ecommerce.order_service.entity.ShippingAddress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingAddressMapper {
    ShippingAddress toShippingAddress(ShippingAddressRequest request);
}