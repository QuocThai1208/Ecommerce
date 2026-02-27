package com.ecommerce.shipment_service.controller;

import com.ecommerce.shipment_service.service.ShopPickupPointService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/shops")
public class ShopController {
    ShopPickupPointService shopService;

}