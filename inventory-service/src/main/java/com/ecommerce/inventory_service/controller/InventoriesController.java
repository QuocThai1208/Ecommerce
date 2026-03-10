package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.ApiResponse;
import com.ecommerce.inventory_service.dto.request.CheckRequest;
import com.ecommerce.inventory_service.dto.response.InventoriesResponse;
import com.ecommerce.inventory_service.dto.response.InventoryTransactionResponse;
import com.ecommerce.inventory_service.repository.InventoriesRepository;
import com.ecommerce.inventory_service.service.InventoriesService;
import com.ecommerce.inventory_service.service.InventoryTransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoriesController {
    InventoryTransactionService transactionService;
    InventoriesService inventoriesService;

    @GetMapping
    ApiResponse<List<InventoriesResponse>> getInventories(){
        return ApiResponse.<List<InventoriesResponse>>builder()
                .message("Get inventories success.")
                .result(inventoriesService.getInventories())
                .build();
    }

    @PostMapping("/by-variants")
    ApiResponse<List<InventoriesResponse>> getByVariantIds(@RequestBody Set<String> variantIds){
        return ApiResponse.<List<InventoriesResponse>>builder()
                .message("Lấy danh sách tồn kho thành công.")
                .result(inventoriesService.getByVariantId(variantIds))
                .build();
    }

    @GetMapping("/{inventoryId}")
    ApiResponse<InventoriesResponse> getInventory(@PathVariable String inventoryId){
        return ApiResponse.<InventoriesResponse>builder()
                .message("Get inventory success.")
                .result(inventoriesService.getInventory(inventoryId))
                .build();
    }

    @GetMapping("/{inventoryId}/transaction")
    ApiResponse<List<InventoryTransactionResponse>> getAllTransactionByInventoryId(@PathVariable String inventoryId){
        return ApiResponse.<List<InventoryTransactionResponse>>builder()
                .message("Get inventory success.")
                .result(transactionService.getAllTransactionByInventoryId(inventoryId))
                .build();
    }

    @GetMapping("/check")
    ApiResponse<Boolean> getInventory(@ModelAttribute CheckRequest request){
        return ApiResponse.<Boolean>builder()
                .message("Get inventory success.")
                .result(inventoriesService.check(request))
                .build();
    }

}