package com.ecommerce.shipment_service.service;

import com.ecommerce.shipment_service.dto.request.UserAddressRequest;
import com.ecommerce.shipment_service.dto.request.UserAddressUpdateRequest;
import com.ecommerce.shipment_service.dto.response.UserAddressResponse;
import com.ecommerce.shipment_service.exception.AppException;
import com.ecommerce.shipment_service.exception.ErrorCode;
import com.ecommerce.shipment_service.mapper.UserAddressMapper;
import com.ecommerce.shipment_service.repository.MasterLocationRepository;
import com.ecommerce.shipment_service.repository.UserAddressRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class UserAddressService {
    UserAddressMapper addressMapper;
    UserAddressRepository addressRepository;

    MasterLocationRepository masterLocationRepository;

    public UserAddressResponse create(UserAddressRequest request){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var now = Instant.now();

        var address = addressMapper.toUserAddress(request);
        var ward = masterLocationRepository.findById(request.getWardCode())
                .orElseThrow(() -> new AppException(ErrorCode.WARD_CODE_NOT_FOUND));

        address.setWardCode(ward);
        var district = ward.getParentCode();
        var province = district.getParentCode();

        address.setUserId(userId);
        address.setCreatedAt(now);
        address.setUpdateAt(now);

        var addressIsDefault = addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElse(null);
        address.setIsDefault(addressIsDefault == null);

        var response =  addressMapper.toUserAddressResponse(addressRepository.save(address));
        response.setWardCode(ward.getCodename());
        response.setDistrictCode(district.getCodename());
        response.setProvinceCode(province.getCodename());
        return response;
    }

    public UserAddressResponse findByUserAndDefaultTrue(){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var userAddress =  addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        return addressMapper.toUserAddressResponse(userAddress);
    }

    public List<UserAddressResponse> findAllByUserId(){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var userAddress = addressRepository.findAllByUserId(userId);

        return userAddress.stream()
                .map(ua -> {
                    var response  = addressMapper.toUserAddressResponse(ua);
                    var ward = ua.getWardCode();
                    var district = ward.getParentCode();
                    var province = district.getParentCode();

                    response.setWardCode(ward.getCodename());
                    response.setDistrictCode(district.getCodename());
                    response.setProvinceCode(province.getCodename());
                    return response;
                }).toList();
    }

    public UserAddressResponse update(UserAddressUpdateRequest request, String userAddressId){
        var now = Instant.now();
        var userAddress = addressRepository.findById(userAddressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        addressMapper.update(userAddress, request);

        if(!userAddress.getWardCode().getCodename().equals(request.getWardCode())){
            var ward = masterLocationRepository.findById(request.getWardCode())
                    .orElseThrow(() -> new AppException(ErrorCode.WARD_CODE_NOT_FOUND));
            userAddress.setWardCode(ward);
        }

        var response = addressMapper.toUserAddressResponse(addressRepository.save(userAddress));

        var district = userAddress.getWardCode().getParentCode();
        var province = district.getParentCode();

        userAddress.setUpdateAt(now);

        response.setWardCode(userAddress.getWardCode().getCodename());
        response.setDistrictCode(district.getCodename());
        response.setProvinceCode(province.getCodename());

        return response;
    }

    @Transactional
    public String setIsDefault(String addressId){
        var now = Instant.now();
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var userAddress = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        if(userAddress.getIsDefault()) return "Địa chỉ đã là mặc định";

        addressRepository.findByUserIdAndIsDefaultTrue(userId)
                        .ifPresent(oldDefault -> {
                            oldDefault.setIsDefault(false);
                            addressRepository.save(oldDefault);
                        });
        userAddress.setIsDefault(true);
        userAddress.setUpdateAt(now);
        addressRepository.save(userAddress);
        return "Chọn địa chỉ mặc định thành công.";
    }

    public String delete(String addressId){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var userAddress = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        if(userAddress.getIsDefault()) throw new AppException(ErrorCode.CAN_NOT_DELETE_ADDRESS_DEFAULT);

        addressRepository.delete(userAddress);
        return "Xóa địa chỉ thành công";
    }
}