package com.ecommerce.shipment_service.controller;


import com.ecommerce.event.dto.ShipmentCreationEvent;
import com.ecommerce.event.dto.WarehouseCreationEvent;
import com.ecommerce.shipment_service.service.ShipmentService;
import com.ecommerce.shipment_service.service.ShopPickupPointService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumer {
    ShipmentService shipmentService;
    ShopPickupPointService shopService;

    @KafkaListener(topics = "order-service.order.create")
    @Transactional
    public void listenerOrderCreate(ShipmentCreationEvent shipmentCreationEvent){
        shipmentService.create(shipmentCreationEvent);
    }

    @KafkaListener(topics = "inventory-service.warehouse.creation")
    public void listenerWarehouseCreation(WarehouseCreationEvent event){
        shopService.registerShop(event);
    }
}