package com.ecommerce.shipment_service.repository.httpClient;

import com.ecommerce.shipment_service.dto.response.DistrictClientResponse;
import com.ecommerce.shipment_service.dto.response.WardClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "location-client",
        url = "https://provinces.open-api.vn/api/"
)
public interface LocationClient {
    @GetMapping("/p/{code}")
    DistrictClientResponse getDistrict(
            @PathVariable("code") long code,
            @RequestParam("depth") long  depth);

    @GetMapping("/d/{code}")
    WardClientResponse getWare(
            @PathVariable("code") long code,
            @RequestParam("depth") long  depth);
}