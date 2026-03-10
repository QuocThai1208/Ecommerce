package com.ecommerce.shipment_service.mapper;

import com.ecommerce.shipment_service.dto.request.UserAddressRequest;
import com.ecommerce.shipment_service.dto.response.UserAddressResponse;
import com.ecommerce.shipment_service.entity.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {
    @Mapping(target = "wardCode", ignore = true)
    UserAddress toUserAddress(UserAddressRequest request);

    @Mapping(target = "wardCode", ignore = true)
    UserAddressResponse toUserAddressResponse(UserAddress userAddress);

    List<UserAddressResponse> toUserAddressResponseList(List<UserAddress> userAddressList);

}