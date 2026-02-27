package com.ecommerce.shipment_service.repository.httpClient;

import com.ecommerce.shipment_service.dto.ApiGhnResponse;
import com.ecommerce.shipment_service.dto.request.GhnCreateOrderRequest;
import com.ecommerce.shipment_service.dto.request.GhnFeeRequest;
import com.ecommerce.shipment_service.dto.request.GhnShopRequest;
import com.ecommerce.shipment_service.dto.request.WardGhnRequest;
import com.ecommerce.shipment_service.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "ghn-client",
        url = "https://dev-online-gateway.ghn.vn/shiip/public-api"
)
public interface GhnClient {
    @GetMapping("/master-data/province")
    ApiGhnResponse<ProvinceGhnResponse> getProvince(@RequestHeader("Token") String token);

    @GetMapping("/master-data/district")
    ApiGhnResponse<DistrictGhnResponse> getDistrict(@RequestHeader("Token") String token);

    @PostMapping("/master-data/ward")
    ApiGhnResponse<WardGhnResponse> getWard(
            @RequestHeader("Token") String token,
            @RequestBody WardGhnRequest request);

    // Đăng ký địa điểm lấy hàng
    @PostMapping(value = "/v2/shop/register", consumes = "application/json")
    GhnShopResponse registerShop(
            @RequestHeader("Token") String token,
            @RequestBody GhnShopRequest request);

    // tạo đơn vận chuyển
    @PostMapping("/v2/shipping-order/create")
    GhnCreateOrderResponse createOrder(
            @RequestHeader("Token") String token,
            @RequestHeader("ShopId") Integer shopId,
            @RequestBody GhnCreateOrderRequest request);

    // Tính phí vận chuyển
    @PostMapping("/v2/shipping-order/fee")
    GhnFeeResponse calculatorFee(
            @RequestHeader("Token") String token,
            @RequestHeader("ShopId") Integer shopId,
            @RequestBody GhnFeeRequest request);
}