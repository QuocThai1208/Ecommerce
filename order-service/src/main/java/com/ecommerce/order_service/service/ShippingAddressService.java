package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.request.ShippingAddressRequest;
import com.ecommerce.order_service.entity.ShippingAddress;
import com.ecommerce.order_service.mapper.ShippingAddressMapper;
import com.ecommerce.order_service.repository.ShippingAddressRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingAddressService {
    ShippingAddressRepository shippingAddressRepository;
    ShippingAddressMapper shippingAddressMapper;

    public ShippingAddress create(ShippingAddressRequest request){
        var shippingAddress = shippingAddressMapper.toShippingAddress(request);
        return shippingAddressRepository.save(shippingAddress);
    }
}