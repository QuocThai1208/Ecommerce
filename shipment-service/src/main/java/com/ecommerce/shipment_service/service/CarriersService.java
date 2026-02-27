package com.ecommerce.shipment_service.service;

import com.ecommerce.shipment_service.dto.request.CarrierCreationRequest;
import com.ecommerce.shipment_service.dto.response.CarrierResponse;
import com.ecommerce.shipment_service.mapper.CarriersMapper;
import com.ecommerce.shipment_service.repository.CarrierRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class CarriersService {
    CarrierRepository carrierRepository;
    CarriersMapper carriersMapper;

    public CarrierResponse create(CarrierCreationRequest request){
        var carrier = carriersMapper.toCarriers(request);
        var now = Instant.now();
        carrier.setCreatedAt(now);
        carrier.setUpdateAt(now);
        return carriersMapper.toCarrierResponse(carrierRepository.save(carrier));
    }
}