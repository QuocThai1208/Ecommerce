package com.ecommerce.order_service.service;

import com.ecommerce.event.dto.*;
import com.ecommerce.order_service.dto.request.*;
import com.ecommerce.order_service.dto.response.*;
import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.enums.MethodType;
import com.ecommerce.order_service.enums.OrderStatus;
import com.ecommerce.order_service.exception.AppException;
import com.ecommerce.order_service.exception.ErrorCode;
import com.ecommerce.order_service.exception.RemoteError;
import com.ecommerce.order_service.mapper.OrderItemMapper;
import com.ecommerce.order_service.mapper.OrdersMapper;
import com.ecommerce.order_service.repository.OrderItemRepository;
import com.ecommerce.order_service.repository.OrdersRepository;
import com.ecommerce.order_service.repository.httpClient.CatalogClient;
import com.ecommerce.order_service.repository.httpClient.InventoryClient;
import com.ecommerce.order_service.repository.httpClient.PaymentClient;
import com.ecommerce.order_service.repository.httpClient.ShipmentClient;
import com.ecommerce.order_service.utility.ErrorUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrdersService {
    OrdersRepository ordersRepository;
    OrderItemRepository orderItemRepository;

    OrdersMapper ordersMapper;
    OrderItemMapper orderItemMapper;

    ConvertDateService convertDateService;

    CatalogClient catalogClient;
    InventoryClient inventoryClient;
    PaymentClient paymentClient;
    ShipmentClient shipmentClient;

    MapRemoteErrorCode mapRemoteErrorCode;

    MapperService mapperService;
    OrderInternalService orderInternalService;
    PaymentService paymentService;

    KafkaTemplate<String, Object> kafkaTemplate;
    Set<OrderStatus> STATUS_CANCEL = EnumSet.of(OrderStatus.PROCESSING, OrderStatus.PENDING, OrderStatus.PAID);

    // Tạo mới order
    public OrderCreationResponse createOrder(OrderCreationRequest request) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Instant now = Instant.now();

        // ***Lấy giá của sản phẩm chạy song song
        CompletableFuture<List<ItemBatchDetailResponse>> catalogFuture = CompletableFuture.supplyAsync(() -> {
            Set<String> ids = request.getBrandOrderRequest().stream()
                    .flatMap(reviewItem -> reviewItem.getProductCheckouts().stream()
                            .map(ProductCheckout::getVariantId))
                    .collect(java.util.stream.Collectors.toSet());
            return catalogClient.getItemBatchDetails(ItemBatchDetailRequest.builder()
                            .productVariantIds(ids)
                            .build())
                    .getResult();
        });

        // Tìm kho tốt nhất để đặt hàng
        var warehouseBestRequest = WarehouseBestRequest.builder()
                .customerLatitude(request.getCustomerLatitude())
                .customerLongitude(request.getCustomerLongitude())
                .productCheckouts(request.getBrandOrderRequest().stream()
                        .flatMap(reviewItem -> reviewItem.getProductCheckouts().stream())
                        .collect(Collectors.toSet()))
                .build();
        var inventoryData = inventoryClient.findBestWarehouses(warehouseBestRequest).getResult();

        // Tính phí ship
        CalculatorFeeRequest calculatorFeeRequest = CalculatorFeeRequest.builder()
                .userAddressId(request.getUserAddressId())
                .calFeeItems(inventoryData.stream()
                        .map(i -> {
                            return CalculatorFeeItemRequest.builder()
                                    .shopPickupPointId(i.getWarehouseId())
                                    .width(1)
                                    .length(1)
                                    .height(1)
                                    .weight(100)
                                    .insuranceValue(1)
                                    .build();
                        }).toList())
                .build();

        var feeData = shipmentClient.calculatorFee(calculatorFeeRequest);
        // <WarehouseId, feeShip>
        Map<String, Integer> feeMap = feeData.getFeeItems().stream()
                .collect(Collectors.toMap(CalculatorFeeItemResponse::getShopPickupPointId, CalculatorFeeItemResponse::getFee));
        // Lấy dữ liệu Catalog đã xong
        List<ItemBatchDetailResponse> catalogDetails = catalogFuture.join();
        // <variantId, ItemBatchDetailResponse>
        Map<String, ItemBatchDetailResponse> catalogMap = catalogDetails.stream()
                .collect(Collectors.toMap(ItemBatchDetailResponse::getProductVariantId, d -> d));


        List<Orders> ordersToSave = new ArrayList<>();
        Map<String, Set<ItemRequest>> orderItemMap = new HashMap<>();
        BigDecimal totalFinalAmount = BigDecimal.ZERO;
        CustomerCheckout customerCheckout = CustomerCheckout.builder()
                .customerLongitude(request.getCustomerLongitude())
                .customerLatitude(request.getCustomerLatitude())
                .build();

        for (var warehouseInfo : inventoryData) {
            String warehouseId = warehouseInfo.getWarehouseId();
            String brandId = warehouseInfo.getBrandId();
            String orderId = UUID.randomUUID().toString();

            Set<ProductCheckout> productCheckoutSet = ordersMapper.toProductCheckoutSet(warehouseInfo.getProductAssignments());
            OrderCheckout orderCheckout = OrderCheckout.builder()
                    .orderId(orderId)
                    .productCheckouts(productCheckoutSet)
                    .build();

            customerCheckout.getOrderCheckouts().add(orderCheckout);

            final String[] currentBrandName = {null};

            Set<ItemRequest> itemRequests = warehouseInfo.getProductAssignments().stream().map(item -> {
                var detail = catalogMap.get(item.getVariantId());
                if (currentBrandName[0] == null) currentBrandName[0] = detail.getBrandName();
                return ItemRequest.builder()
                        .productVariantId(detail.getProductVariantId())
                        .productNameSnapshot(detail.getProductNameSnapshot())
                        .unitPriceSnapshot(detail.getUnitPriceSnapshot())
                        .mediaUrl(detail.getMediaUrl())
                        .quantity(item.getQuantity())
                        .build();
            }).collect(Collectors.toSet());

            // Tính tiền cho kiện hàng tại kho này
            BigDecimal subtotal = itemRequests.stream()
                    .map(i -> i.getUnitPriceSnapshot().multiply(BigDecimal.valueOf(i.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal shippingFee = BigDecimal.valueOf(feeMap.getOrDefault(warehouseId, 0));
            BigDecimal finalAmount = subtotal.add(shippingFee);
            totalFinalAmount = totalFinalAmount.add(finalAmount);

            Orders order = Orders.builder()
                    .id(orderId)
                    .userId(userId)
                    .warehouseId(warehouseId)
                    .brandId(brandId)
                    .brandName(currentBrandName[0])
                    .status(OrderStatus.PENDING)
                    .subtotalAmount(subtotal)
                    .shippingCost(shippingFee)
                    .finalAmount(finalAmount)
                    .method(request.getMethod())
                    .createdAt(now)
                    .updateAt(now)
                    .build();
            ordersToSave.add(order);
            orderItemMap.put(orderId, itemRequests);
        }

        try{
            var resultReserve = inventoryClient.reserve(customerCheckout).getResult();
            if(!resultReserve) throw new AppException(ErrorCode.RESERVE_FAIL);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        var orders = orderInternalService.saveMultipleOrder(ordersToSave, orderItemMap);

        var response = OrderCreationResponse.builder()
                .status(OrderStatus.PENDING)
                .finalAmount(totalFinalAmount)
                .build();
        switch (request.getMethod()) {
            case BANK_TRANSFER:
                List<TransactionDetailRequest> transactionDetailList = orders.stream()
                        .map(order -> {
                            return TransactionDetailRequest.builder()
                                    .orderId(order.getId())
                                    .brandId(order.getBrandId())
                                    .amount(order.getFinalAmount())
                                    .products(mapperService.toProductCheckoutRequests(orderItemMap.get(order.getId())))
                                    .build();
                        }).toList();
                TransactionCreationRequest transactionCreation = TransactionCreationRequest.builder()
                        .userId(userId)
                        .method(request.getMethod())
                        .transactionDetailRequests(transactionDetailList)
                        .build();
                String url = handleStripePayment(transactionCreation);
                response.setSessionUrl(url);
//                outEventToShipmentService(
//                        itemRequests,
//                        orderId,
//                        request.getCarrierId(),
//                        request.getUserAddressId(),
//                        request.getShopPickupPointId(),
//                        BigDecimal.ZERO);
                break;
            case CASH:
                List<CashPaymentItemEvent> cashItemEventList = orders.stream()
                        .map(order -> {
                            return CashPaymentItemEvent.builder()
                                    .orderId(order.getId())
                                    .brandId(order.getBrandId())
                                    .amount(order.getFinalAmount())
                                    .build();
                        }).toList();
                CashPaymentEvent cashEvent = CashPaymentEvent.builder()
                        .userId(userId)
                        .method(request.getMethod())
                        .cashPaymentItemEvents(cashItemEventList)
                        .build();
                handleCashPayment(cashEvent);
//                outEventToShipmentService(
//                        itemRequests,
//                        orderId,
//                        request.getCarrierId(),
//                        request.getUserAddressId(),
//                        request.getShopPickupPointId(),
//                        order.getFinalAmount().subtract(order.getShippingCost()));
                break;
        }
        return response;
    }

    // Lấy thông tin chi tiết order
    public OrderDetailResponse getDetailOrder(String orderId) {
        var order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        return ordersMapper.toOrderDetailResponse(order);
    }

    // Lấy danh sách order theo userId
    public PageResponse<PurchaseResponse> getOrderByUserId(int page,
                                                               int size,
                                                               OrderStatus status,
                                                               String fromDate,
                                                               String toDate) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Instant startDate = convertDateService.convertToStartOfDayInstant(fromDate);
        Instant endDate = convertDateService.convertToEndOfDayInstant(toDate);
        Pageable pageable = PageRequest.of(page - 1, size); // Khởi tạo đối tượng phân trang

        var pageData = ordersRepository.findByFilter(status, userId, startDate, endDate, pageable);

        var purchaseResponse = ordersMapper.toPurchaseResponses(pageData.getContent());

        List<String> orderIds = purchaseResponse.stream()
                .map(PurchaseResponse::getId)
                .toList();

        var allItems = orderItemRepository.findAllByOrderIdIn(orderIds);

        Map<String, List<PurchaseItemResponse>> itemsMap = allItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getOrder().getId(), // Giả sử entity có field này
                        Collectors.mapping(orderItemMapper::toPurchaseItemResponse, Collectors.toList())
                ));

        purchaseResponse.forEach(p -> p.setPurchaseItems(itemsMap.getOrDefault(p.getId(), Collections.emptyList())));

        return PageResponse.<PurchaseResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(purchaseResponse)
                .build();
    }

    // Update status
    public OrderStatusResponse updateStatus(String orderId, NewStatusRequest request) {
        var order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        order.setStatus(request.getStatus());
        order.setUpdateAt(Instant.now());
        return ordersMapper.toOrderStatusResponse(ordersRepository.save(order));
    }

    // Hủy order
    @Transactional
    public OrderStatusResponse cancelOrder(String orderId, OrderCancelRequest request) {
        var order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        if (!STATUS_CANCEL.contains(order.getStatus()))
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_CANCEL);
        if (order.getStatus() == OrderStatus.CANCELLING) throw new AppException(ErrorCode.ORDER_BEING_PROCESSED);

        // tạo payment refund
        paymentService.refundPayment(
                order.getId());

        // thực hiện hoàn tiền
        CancelEvent cancelEvent = CancelEvent.builder()
                .orderId(orderId)
                .email(request.getEmail())
                .reason(request.getReason())
                .amount(order.getFinalAmount())
                .build();
        // Gửi sự kiện qua cho payment service
        kafkaTemplate.send("order-service.order.payment.cancel", cancelEvent);

        order.setStatus(OrderStatus.CANCELLING);
        order = ordersRepository.save(order);

        return ordersMapper.toOrderStatusResponse(order);
    }

    @EventListener
    public void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        String orderId = event.getOrderId();
        NewStatusRequest status = NewStatusRequest.builder()
                .status(event.getStatus())
                .build();

        try {
            updateStatus(orderId, status);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UPDATE_ORDER_STATUS_ERROR);
        }
    }

    // Lấy danh sách đơn hàng theo bộ lọc
    public PageResponse<OrderSummaryResponse> getOrdersByFilter(int page,
                                                                int size,
                                                                OrderStatus status,
                                                                String userId,
                                                                String fromDate,
                                                                String toDate) {
        Instant startDate = convertDateService.convertToStartOfDayInstant(fromDate);
        Instant endDate = convertDateService.convertToEndOfDayInstant(toDate);

        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = ordersRepository.findByFilter(status, userId, startDate, endDate, pageable);

        return PageResponse.<OrderSummaryResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(ordersMapper.toOrdersResponseList(pageData.getContent()))
                .build();
    }

    public List<OrderReviewResponse> review(OrderReviewRequest request) {
        try {
            // ***Lấy giá của sản phẩm và tạo dịa chỉ: chạy song song
            CompletableFuture<List<ProductCheckoutResponse>> catalogFuture = CompletableFuture.supplyAsync(() -> {
                Set<String> ids = request.getBrandOrderRequest().stream()
                        .flatMap(reviewItem -> reviewItem.getProductCheckouts().stream())
                        .map(ProductCheckout::getVariantId)
                        .collect(Collectors.toSet());

                return catalogClient.getCheckout(ids)
                        .getResult();
            });

            // Tìm kho tốt nhất để đặt hàng
            var warehouseBestRequest = ordersMapper.toWarehouseBestRequest(request);
            warehouseBestRequest.setProductCheckouts(request.getBrandOrderRequest().stream()
                    .flatMap(reviewItem -> reviewItem.getProductCheckouts().stream())
                    .collect(Collectors.toSet()));
            var inventoryData = inventoryClient.findBestWarehouses(warehouseBestRequest).getResult();

            // Tính phí ship
            CalculatorFeeRequest calculatorFeeRequest = CalculatorFeeRequest.builder()
                    .userAddressId(request.getUserAddressId())
                    .calFeeItems(inventoryData.stream()
                            .map(i -> {
                                return CalculatorFeeItemRequest.builder()
                                        .shopPickupPointId(i.getWarehouseId())
                                        .width(1)
                                        .length(1)
                                        .height(1)
                                        .weight(10)
                                        .insuranceValue(1)
                                        .build();
                            }).toList())
                    .build();

            var feeData = shipmentClient.calculatorFee(calculatorFeeRequest);

            List<ProductCheckoutResponse> catalogData = catalogFuture.join();

            Map<String, Integer> feeMap = feeData.getFeeItems().stream()
                    .collect(Collectors.toMap(CalculatorFeeItemResponse::getShopPickupPointId, CalculatorFeeItemResponse::getFee));

            // Tạo Map tra cứu nhanh: variantId -> số lượng được giao từ Inventory
            Map<String, Long> variantQuantityMap = inventoryData.stream()
                    .flatMap(i -> i.getProductAssignments().stream())
                    .collect(Collectors.groupingBy(ProductAssignment::getVariantId, Collectors.summingLong(ProductAssignment::getQuantity)));

            return catalogData.stream().map(brandPackage -> {
                long totalBrandFee = inventoryData.stream()
                        .filter(i -> i.getBrandId().equals(brandPackage.getBrandId()))
                        .mapToLong(i -> feeMap.getOrDefault(i.getWarehouseId(), 0))
                        .sum();

                List<ProductReviewResponse> products = brandPackage.getItemCheckoutResponses().stream()
                        .map(item -> {
                            long quantity = variantQuantityMap.getOrDefault(item.getVariantId(), 0L);
                            return ProductReviewResponse.builder()
                                    .variantId(item.getVariantId())
                                    .productName(item.getProductName())
                                    .variantName(item.getVariantName())
                                    .price(item.getUnitPriceSnapshot())
                                    .quantity(quantity)
                                    .total(item.getUnitPriceSnapshot() * quantity)
                                    .image(item.getImage())
                                    .build();
                        }).toList();
                return OrderReviewResponse.builder()
                        .brandId(brandPackage.getBrandId())
                        .brandName(brandPackage.getBrandName())
                        .badge("Mall")
                        .feeShip(totalBrandFee)
                        .products(products)
                        .build();
            }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // tính trước tổng số tiền của đơn hàng
    private BigDecimal calculateSubtotalAmountReview(Set<OrderItemRequest> requests, String fee) {
        BigDecimal[] subtotal = {BigDecimal.ZERO};
        requests.forEach(request -> {
            BigDecimal quantity = BigDecimal.valueOf(request.getQuantity());
            BigDecimal unitPrice = request.getUnitPriceSnapshot();
            subtotal[0] = subtotal[0].add(quantity.multiply(unitPrice));
        });
        subtotal[0].add(BigDecimal.valueOf(Long.parseLong(fee)));
        return subtotal[0].setScale(2, RoundingMode.HALF_UP);
    }

    // gọi payment service để lấy url thanh toán của stripe
    private String handleStripePayment(TransactionCreationRequest transactionCreation) {
        try {
            var response = paymentClient.createTransaction(transactionCreation);
            var paymentResponseData = response.getResult();
            return paymentResponseData.getSessionUrl();
        } catch (Exception e) {
            // gửi event lên kafka để hủy đơn hàng vừa đặt
            transactionCreation.getTransactionDetailRequests().forEach(t -> {
                kafkaTemplate.send("order-service.payment.transaction.error",
                        ReleaseEvent.builder()
                                .orderId(t.getOrderId())
                                .build());
            });
            Optional<RemoteError> remoteErrorOpt = ErrorUtils.parseRemoteError(e.getMessage());
            if (remoteErrorOpt.isPresent()) {
                RemoteError remoteError = remoteErrorOpt.get();
                ErrorCode internalErrorCode = mapRemoteErrorCode.mapRemoteErrorCode(remoteError.code());
                throw new AppException(internalErrorCode);
            } else {
                throw new AppException(ErrorCode.ACCESS_ERROR);
            }
        }
    }

    private void handleCashPayment(CashPaymentEvent cashPaymentEvent) {
        kafkaTemplate.send("order-service.order.cash-payment", cashPaymentEvent);
    }

//    private void outEventToShipmentService(
//            Set<ItemRequest> itemRequests,
//            String orderId,
//            String carrierId,
//            String userAddressId,
//            String shopPickupPointId,
//            BigDecimal codeAmount) {
//        List<GhnItem> ghnItems = itemRequests.stream()
//                .map(i -> {
//                    return GhnItem.builder()
//                            .name(i.getProductNameSnapshot())
//                            .price(i.getUnitPriceSnapshot().intValue())
//                            .quantity((int) i.getQuantity())
//                            .build();
//                })
//
//                .toList();
//        ShipmentCreationEvent shipmentEvent = ShipmentCreationEvent.builder()
//                .orderId(orderId)
//                .carrierId(carrierId)
//                .codAmount(codeAmount)
//                .shopPickupPointId(shopPickupPointId)
//                .userAddressId(userAddressId)
//                .items(ghnItems)
//                .build();
//        kafkaTemplate.send("order-service.order.create", shipmentEvent);
//    }
}