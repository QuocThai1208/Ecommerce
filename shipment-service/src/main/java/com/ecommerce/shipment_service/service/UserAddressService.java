package com.ecommerce.shipment_service.service;

import com.ecommerce.shipment_service.dto.request.UserAddressRequest;
import com.ecommerce.shipment_service.dto.response.UserAddressResponse;
import com.ecommerce.shipment_service.entity.UserAddress;
import com.ecommerce.shipment_service.exception.AppException;
import com.ecommerce.shipment_service.exception.ErrorCode;
import com.ecommerce.shipment_service.mapper.UserAddressMapper;
import com.ecommerce.shipment_service.repository.MasterLocationRepository;
import com.ecommerce.shipment_service.repository.UserAddressRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class UserAddressService {
    UserAddressMapper userAddressMapper;
    UserAddressRepository userAddressRepository;

    MasterLocationRepository masterLocationRepository;

    public UserAddressResponse create(UserAddressRequest request){
        var address = userAddressMapper.toUserAddress(request);
        log.info("wardCode: {}", request.getWareCode());
        address.setWareCode(masterLocationRepository.findById(request.getWareCode())
                .orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_FOUND)));
        var ward = address.getWareCode();
        var district = ward.getParentCode();
        var province = district.getParentCode();
        var now = Instant.now();
        address.setCreatedAt(now);
        address.setUpdateAt(now);

        var addressIsDefault = userAddressRepository.findByUserIdAndIsDefaultTrue(request.getUserId())
                .orElse(null);
        address.setIsDefault(addressIsDefault == null);

        var response = userAddressMapper.toUserAddressResponse(userAddressRepository.save(address));
        response.setAddress(String.join(", ",
                address.getAddressDetail(),
                ward.getName(),
                district.getName(),
                province.getName()));
        return response;
    }
}