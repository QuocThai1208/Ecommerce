package com.ecommerce.shipment_service.mapper;

import com.ecommerce.shipment_service.dto.request.UserAddressRequest;
import com.ecommerce.shipment_service.dto.response.UserAddressResponse;
import com.ecommerce.shipment_service.entity.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {
    @Mapping(target = "wareCode", ignore = true)
    UserAddress toUserAddress(UserAddressRequest request);
    UserAddressResponse toUserAddressResponse(UserAddress userAddress);
}