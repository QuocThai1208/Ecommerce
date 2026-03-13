package com.ecommerce.inventory_service.service;


import com.ecommerce.event.dto.InflowEvent;
import com.ecommerce.inventory_service.dto.request.*;
import com.ecommerce.inventory_service.dto.response.InventoryTransactionResponse;
import com.ecommerce.inventory_service.dto.response.ProductAssignment;
import com.ecommerce.inventory_service.dto.response.WarehouseBestResponse;
import com.ecommerce.inventory_service.entity.Inventories;
import com.ecommerce.inventory_service.entity.InventoryTransaction;
import com.ecommerce.inventory_service.entity.Warehouse;
import com.ecommerce.inventory_service.exception.AppException;
import com.ecommerce.inventory_service.exception.ErrorCode;
import com.ecommerce.inventory_service.mapper.InventoryTransactionMapper;
import com.ecommerce.inventory_service.repository.InventoriesRepository;
import com.ecommerce.inventory_service.repository.InventoryTransactionRepository;
import com.ecommerce.inventory_service.repository.WarehouseRepository;
import com.ecommerce.inventory_service.type.TransactionType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryTransactionService {
    InventoryTransactionRepository inventoryTransactionRepository;
    InventoriesRepository inventoriesRepository;
    WarehouseRepository warehouseRepository;
    InventoryTransactionMapper inventoryTransactionMapper;

    InventoriesService inventoriesService;
    KafkaTemplate<String, Object> kafkaTemplate;

    // Lấy danh sách transaction then invId
    public List<InventoryTransactionResponse> getAllTransactionByInventoryId(String inventoryId) {
        var inventory = inventoriesRepository.findById(inventoryId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_EXISTED));
        List<InventoryTransaction> transactions = inventoryTransactionRepository.findByInventory(inventory);
        return inventoryTransactionMapper.toInventoryTransactionResponseList(transactions);
    }

    // Nhập kho
    public InventoryTransactionResponse inflow(InventoryTransactionRequest request) {
        if (request.getQualityChange() <= 0) throw new AppException(ErrorCode.QUANTITY_INFLOW_INVALID);

        var transaction = inventoryTransactionMapper.toInventoryTransaction(request);
        var warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED));

        var inventory = inventoriesRepository.findByProductVariantIdAndWarehouse(request.getProductVariantId(), warehouse)
                .orElseGet(() -> {
                    kafkaTemplate.send("inventory.inflow.first", InflowEvent.builder()
                            .variantId(request.getProductVariantId())
                            .build());
                    return inventoriesService.create(InventoriesRequest.builder()
                            .productVariantId(request.getProductVariantId())
                            .quantityAvailable(0)
                            .quantityReserved(0)
                            .warehouse(warehouse)
                            .build());
                });

        inventoriesService.inflow(inventory, request.getQualityChange());

        transaction.setInventory(inventory);
        transaction.setTransactionType(TransactionType.GOODS_RECEIPT);
        transaction.setCreated_at(Instant.now());
        transaction.setUpdate_at(Instant.now());
        transaction = inventoryTransactionRepository.save(transaction);

        return inventoryTransactionMapper.toInventoryTransactionResponse(transaction);
    }

    // Đặt hàng trước
    @Transactional
    public void reserve(Set<InventoryTransactionRequest> requests, String orderId) {
        List<InventoryTransaction> transactionsToSave = new ArrayList<>();
        var now = Instant.now();
        requests.forEach(request -> {
            if (request.getQualityChange() <= 0) throw new AppException(ErrorCode.QUANTITY_ORDER_INVALID);

            var warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED));

            var inventory = inventoriesRepository.findByProductVariantIdAndWarehouse(request.getProductVariantId(), warehouse)
                    .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_EXISTED));

            inventoriesService.reserve(inventory, request.getQualityChange());

            var transactionAvailable = inventoryTransactionMapper.toInventoryTransaction(request);
            var transactionReserved = inventoryTransactionMapper.toInventoryTransaction(request);

            transactionAvailable.setInventory(inventory);
            transactionReserved.setInventory(inventory);

            transactionAvailable.setQualityChange(-request.getQualityChange());

            transactionAvailable.setTransactionType(TransactionType.RESERVATION);
            transactionReserved.setTransactionType(TransactionType.RESERVATION);

            transactionAvailable.setOrderId(orderId);
            transactionReserved.setOrderId(orderId);

            transactionAvailable.setCreated_at(now);
            transactionReserved.setCreated_at(now);
            transactionAvailable.setUpdate_at(now);
            transactionReserved.setUpdate_at(now);

            transactionsToSave.add(transactionAvailable);
            transactionsToSave.add(transactionReserved);
        });

        inventoryTransactionMapper.toInventoryTransactionResponseList(
                inventoryTransactionRepository.saveAll(transactionsToSave)
        );
    }

    // Hủy đặc trước
    @Transactional
    public String release(String orderId) {
        List<InventoryTransaction> transactionList = inventoryTransactionRepository.findAllByOrderId(orderId);
        if(transactionList.isEmpty()) return "Không tìm thấy thông tin xuất kho của sản phẩm.";

        var now = Instant.now();

        List<Inventories> inventoriesToSave = new ArrayList<>();
        List<InventoryTransaction> transactionsToSave = new ArrayList<>();

        transactionList.forEach(t -> {
            var inventory = t.getInventory();
            long quantityChange = t.getQualityChange();

            if(quantityChange < 0){
                // trừ cột giữ hàng nếu quantityChange < 0
                inventory.setQuantityReserved(inventory.getQuantityReserved() + quantityChange);
            }
            else{
                // ngược lại tăng cột hàng có sẳn
                inventory.setQuantityAvailable(inventory.getQuantityAvailable() + quantityChange);
            }
            inventory.setUpdate_at(now);
            inventoriesToSave.add(inventory);

            transactionsToSave.add(InventoryTransaction.builder()
                    .inventory(inventory)
                    .qualityChange(quantityChange)
                    .transactionType(TransactionType.RESERVATION_RELEASE)
                    .orderId(orderId)
                    .created_at(now)
                    .update_at(now)
                    .build());
        });
        inventoriesRepository.saveAll(inventoriesToSave);
        inventoryTransactionRepository.saveAll(transactionsToSave);
        return "Hủy đơn hàng thành công";
    }


    // Xuất kho trực tiếp
    public InventoryTransactionResponse DirectOutflow(InventoryTransactionRequest request) {
        if (request.getQualityChange() <= 0) throw new AppException(ErrorCode.QUANTITY_INFLOW_INVALID);

        var transaction = inventoryTransactionMapper.toInventoryTransaction(request);
        var warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED));

        var inventory = inventoriesRepository.findByProductVariantIdAndWarehouse(request.getProductVariantId(), warehouse)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_EXISTED));

        inventoriesService.directOutflow(inventory, request.getQualityChange());

        transaction.setInventory(inventory);
        transaction.setQualityChange(-request.getQualityChange());
        transaction.setTransactionType(TransactionType.ISSUED_FOR_PRODUCTION);
        transaction.setCreated_at(Instant.now());
        transaction.setUpdate_at(Instant.now());

        return inventoryTransactionMapper.toInventoryTransactionResponse(inventoryTransactionRepository.save(transaction));
    }

    // Xuất kho từ hàng đã đặt
    public InventoryTransactionResponse outflow(InventoryTransactionRequest request) {
        if (request.getQualityChange() <= 0) throw new AppException(ErrorCode.QUANTITY_INFLOW_INVALID);

        var transaction = inventoryTransactionMapper.toInventoryTransaction(request);
        var warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED));

        var inventory = inventoriesRepository.findByProductVariantIdAndWarehouse(request.getProductVariantId(), warehouse)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_EXISTED));

        inventoriesService.outflow(inventory, request.getQualityChange());

        transaction.setInventory(inventory);
        transaction.setQualityChange(-request.getQualityChange());
        transaction.setTransactionType(TransactionType.ISSUED_FOR_PRODUCTION);
        transaction.setCreated_at(Instant.now());
        transaction.setUpdate_at(Instant.now());

        return inventoryTransactionMapper.toInventoryTransactionResponse(inventoryTransactionRepository.save(transaction));
    }

    // Đặt hàng theo kho hàng gần với khách hàng nhất
    @Transactional
    public Boolean reserveMultipleOrder(CustomerCheckout request) {
       // Gom tất cả các sản phẩm để tìm kho trong 1 lần
        Set<ProductCheckout> allProducts = request.getOrderCheckouts().stream()
                .flatMap(oc -> oc.getProductCheckouts().stream())
                .collect(Collectors.toSet());

        // Tìm kho cần lấy hàng
        List<WarehouseBestResponse> bestAssignments = findBestWarehouses(WarehouseBestRequest.builder()
                .customerLatitude(request.getCustomerLatitude())
                .customerLongitude(request.getCustomerLongitude())
                .productCheckouts(allProducts)
                .build());

        // Tạo Map tra cứu VariantId -> WarehouseId
        Map<String, String> variantToWarehouseMap = new HashMap<>();
        bestAssignments.forEach(res ->
                res.getProductAssignments().forEach(pa ->
                        variantToWarehouseMap.put(pa.getVariantId(), res.getWarehouseId())
                )
        );

        for(OrderCheckout order : request.getOrderCheckouts()){
            Set<InventoryTransactionRequest> itRequests = order.getProductCheckouts().stream()
                    .map(pc -> {
                        String wId = variantToWarehouseMap.get(pc.getVariantId());
                        if (wId == null) {
                            throw new AppException(ErrorCode.VARIANT_NOT_EXISTED);
                        }
                        return InventoryTransactionRequest.builder()
                                .warehouseId(wId)
                                .productVariantId(pc.getVariantId())
                                .qualityChange(pc.getQuantity())
                                .build();
                    }).collect(Collectors.toSet());
            reserve(itRequests, order.getOrderId());
        }
        return true;
    }

    public List<WarehouseBestResponse> findBestWarehouses(WarehouseBestRequest request){
        // 1. Lấy danh sách variantId và số lượng cần thiết
        Map<String, Long> requirements = request.getProductCheckouts().stream()
                .collect(Collectors.toMap(ProductCheckout::getVariantId, ProductCheckout::getQuantity));

        // 2. Lấy danh sách các bản ghi Inventories thỏa mãn
        List<Inventories> availableStocks = inventoriesRepository.findAllByProductVariantIdIn(requirements.keySet());
        if(availableStocks.isEmpty()) throw new AppException(ErrorCode.VARIANT_NOT_EXISTED);

        double custLat = Double.parseDouble(request.getCustomerLatitude());
        double custLon = Double.parseDouble(request.getCustomerLongitude());

        Map<String, Map<String, Long>> warehouseStockMap = new LinkedHashMap<>();

        Map<String, String> warehouseByBrandMap = new HashMap<>();

        // Tính khoảng cách 1 lần duy nhất cho mỗi kho
        Map<String, Double> distanceMap = new HashMap<>();

        // Gom nhóm và tính distance
        availableStocks.forEach(inv -> {
            Warehouse w = inv.getWarehouse();
            String wId = inv.getWarehouse().getId();

            warehouseByBrandMap.putIfAbsent(wId, w.getBrandId());

            warehouseStockMap.computeIfAbsent(wId, k -> {
                distanceMap.put(k, CalculateDistance.calculateDistance(
                        inv.getWarehouse().getLatitude(), inv.getWarehouse().getLongitude(), custLat, custLon));
                return new HashMap<>();
            }).put(inv.getProductVariantId(), inv.getQuantityAvailable());
        });

        // Sắp xếp WarehouseId dựa trên distanceMap đã tính sẵn
        List<String> sortedWarehouseIds = warehouseStockMap.keySet().stream()
                .sorted(Comparator.comparingDouble(distanceMap::get))
                .collect(Collectors.toList());

        // 4. Phân bổ sản phẩm
        List<WarehouseBestResponse> response = new ArrayList<>();
        Map<String, Long> remainingNeeds = new HashMap<>(requirements);

        // CHIẾN LƯỢC 1: Tìm kho đơn lẻ
        for (String wId : sortedWarehouseIds) {
            Map<String, Long> stocks = warehouseStockMap.get(wId);
            boolean canFulfillAll = remainingNeeds.entrySet().stream()
                    .allMatch(req -> stocks.getOrDefault(req.getKey(), 0L) >= req.getValue());

            if (canFulfillAll) {
                Set<ProductAssignment> assignments = remainingNeeds.entrySet().stream()
                        .map(req -> new ProductAssignment(req.getKey(), req.getValue()))
                        .collect(Collectors.toSet());
                return List.of(new WarehouseBestResponse(wId, warehouseByBrandMap.get(wId), assignments));
            }
        }

        //CHIẾN LƯỢC 2: Tách kho (Dùng Iterator kết hợp Map lookup)
        for (String wId : sortedWarehouseIds) {
            Map<String, Long> stocks = warehouseStockMap.get(wId);
            Set<ProductAssignment> pickedInThisWarehouse = new HashSet<>();

            Iterator<Map.Entry<String, Long>> it = remainingNeeds.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Long> req = it.next();
                Long available = stocks.getOrDefault(req.getKey(), 0L);

                if (available >= req.getValue()) {
                    pickedInThisWarehouse.add(new ProductAssignment(req.getKey(), req.getValue()));
                    it.remove();
                }
            }

            if (!pickedInThisWarehouse.isEmpty()) {
                response.add(new WarehouseBestResponse(wId, warehouseByBrandMap.get(wId), pickedInThisWarehouse));
            }
            if (remainingNeeds.isEmpty()) break;
        }
        if (!remainingNeeds.isEmpty()) {
            throw new AppException(ErrorCode.FILTER_SUFFICIENT_INVENTORIES_ERROR);
        }
        return response;
    }
}