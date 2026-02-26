package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.ApiResponse;
import com.ecommerce.inventory_service.dto.request.InventoryReleaseRequest;
import com.ecommerce.inventory_service.dto.request.InventoryTransactionRequest;
import com.ecommerce.inventory_service.dto.request.WarehouseBestRequest;
import com.ecommerce.inventory_service.dto.response.InventoryTransactionResponse;
import com.ecommerce.inventory_service.dto.response.WarehouseBestResponse;
import com.ecommerce.inventory_service.service.InventoryTransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    InventoryTransactionService inventoryTransactionService;

    @PostMapping("/best-warehouse")
    ApiResponse<List<WarehouseBestResponse>> findBestWarehouses(@RequestBody WarehouseBestRequest request){
        return ApiResponse.<List<WarehouseBestResponse>>builder()
                .message("Tìm dánh sách kho tối ưu thành công.")
                .result(inventoryTransactionService.findBestWarehouses(request))
                .build();
    }

    @PostMapping("/inflow")
    ApiResponse<InventoryTransactionResponse> inflow(@RequestBody InventoryTransactionRequest request){
        return ApiResponse.<InventoryTransactionResponse>builder()
                .message("Inflow inventory success.")
                .result(inventoryTransactionService.inflow(request))
                .build();
    }

    @PostMapping("/release")
    ApiResponse<String> release(@RequestBody InventoryReleaseRequest request){
        return ApiResponse.<String>builder()
                .message("Hủy đơn hành thành công.")
                .result(inventoryTransactionService.release(request.getOrderId()))
                .build();
    }

    @PostMapping("/direct-outflow")
    ApiResponse<InventoryTransactionResponse> directOutflow(@RequestBody InventoryTransactionRequest request){
        return ApiResponse.<InventoryTransactionResponse>builder()
                .message("Direct outflow inventory success.")
                .result(inventoryTransactionService.DirectOutflow(request))
                .build();
    }

    @PostMapping("/outflow")
    ApiResponse<InventoryTransactionResponse> outflow(@RequestBody InventoryTransactionRequest request){
        return ApiResponse.<InventoryTransactionResponse>builder()
                .message("Outflow inventory success.")
                .result(inventoryTransactionService.outflow(request))
                .build();
    }
}