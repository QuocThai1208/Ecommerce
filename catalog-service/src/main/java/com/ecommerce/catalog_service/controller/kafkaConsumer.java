package com.ecommerce.catalog_service.controller;


import com.ecommerce.catalog_service.enums.ProductStatus;
import com.ecommerce.catalog_service.service.ProductVariantService;
import com.ecommerce.event.dto.InflowEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class kafkaConsumer {
    ProductVariantService variantService;

    @KafkaListener(topics = "inventory.inflow.first")
    public void listenerInflowFirst(InflowEvent event){
        log.info("inventory.inflow.first");
        variantService.updateStatus(event.getVariantId(), ProductStatus.ACTIVE);
    }
}
