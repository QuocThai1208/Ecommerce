package com.ecommerce.shipment_service.controller;

import com.ecommerce.shipment_service.dto.request.CalculatorFeeRequest;
import com.ecommerce.shipment_service.dto.response.CalculatorFeeResponse;
import com.ecommerce.shipment_service.dto.response.GhnFeeResponse;
import com.ecommerce.shipment_service.service.GhnShipmentService;
import com.ecommerce.shipment_service.service.ShipmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/shipments")
public class ShipmentController {
    GhnShipmentService ghnShipmentService;
    ShipmentService shipmentService;

    @PostMapping("/calculator-fee")
    CalculatorFeeResponse calculatorFee(@RequestBody CalculatorFeeRequest request){
        return shipmentService.calculatorFee(request);
    }
}