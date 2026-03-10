package com.ecommerce.order_service.service;

import com.ecommerce.event.dto.*;
import com.ecommerce.order_service.dto.request.*;
import com.ecommerce.order_service.dto.response.*;
import com.ecommerce.order_service.entity.OrderPayment;
import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.enums.MethodType;
import com.ecommerce.order_service.enums.OrderStatus;
import com.ecommerce.order_service.enums.PaymentStatus;
import com.ecommerce.order_service.enums.TransactionType;
import com.ecommerce.order_service.exception.AppException;
import com.ecommerce.order_service.exception.ErrorCode;
import com.ecommerce.order_service.exception.RemoteError;
import com.ecommerce.order_service.mapper.OrdersMapper;
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
import org.springframework.core.annotation.Order;
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

    OrdersMapper ordersMapper;

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
        var orderId = UUID.randomUUID().toString();
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // *** Tính phí vận chuyển
        CompletableFuture<GhnFeeResponse> calculatorFee = CompletableFuture.supplyAsync(()->{
            return shipmentClient.calculatorFee(CalculatorFeeRequest.builder()
                    .carrierId(request.getCarrierId())
                    .shopPickupPointId(request.getShopPickupPointId())
                    .userAddressId(request.getUserAddressId())
                    .width(0)
                    .weight(0)
                    .height(0)
                    .length(0)
                    .insuranceValue(0)
                    .build());
        });
        // ***Lấy giá của sản phẩm và tạo dịa chỉ: chạy song song
        CompletableFuture<List<ItemBatchDetailResponse>> catalogFuture = CompletableFuture.supplyAsync(() -> {
            Set<String> ids = request.getOrderItemRequest().stream()
                    .map(OrderItemRequest::getProductVariantId)
                    .collect(java.util.stream.Collectors.toSet());
            return catalogClient.getItemBatchDetails(ItemBatchDetailRequest.builder()
                            .productVariantIds(ids)
                            .build())
                    .getResult();
        });
        // *** Gọi inventory service để đặt hàng, chạy song song
        CompletableFuture<Void> inventoryFuture = CompletableFuture.runAsync(() -> {
            //Build items
            Set<InventoryReservationRequest.ItemCheck> items = request.getOrderItemRequest().stream()
                    .map(r -> new InventoryReservationRequest.ItemCheck(
                            r.getProductVariantId(),
                            r.getQuantity())
                    ).collect(Collectors.toSet());
            // gọi inventory service
            inventoryClient.reserve(InventoryReservationRequest.builder()
                    .customerLatitude(String.valueOf(request.getShippingAddressRequest().getLatitude()))
                    .customerLongitude(String.valueOf(request.getShippingAddressRequest().getLongitude()))
                    .orderId(orderId)
                    .items(items)
                    .build());
        });
        // ***tạo order
        var order = ordersMapper.toOrders(request);
        order.setId(orderId);
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);

        order.setCreatedAt(Instant.now());
        order.setUpdateAt(Instant.now());

        // ***tạo danh sách các sản phẩm
        // Map tra cứu productVariantId-quantity
        Map<String, Long> quantityLookup = request.getOrderItemRequest().stream()
                .collect(Collectors.toMap(OrderItemRequest::getProductVariantId, OrderItemRequest::getQuantity));

        List<ItemBatchDetailResponse> itemBatchDetails;
        try {
            itemBatchDetails = catalogFuture.join();
        } catch (Exception e) {
            throw new AppException(ErrorCode.CATALOG_SERVICE_ERROR);
        }
        // Build ItemRequest
        Set<ItemRequest> itemRequests = ordersMapper.toItemRequestSet(new HashSet<>(itemBatchDetails));
        itemRequests.forEach(i -> {
            if (quantityLookup.containsKey(i.getProductVariantId())) {
                i.setQuantity(quantityLookup.get(i.getProductVariantId()));
            }
        });

        try {
            inventoryFuture.join();
        } catch (Exception e) {
            Optional<RemoteError> remoteErrorOpt = ErrorUtils.parseRemoteError(e.getMessage());
            if (remoteErrorOpt.isPresent()) {
                RemoteError remoteError = remoteErrorOpt.get();
                ErrorCode internalErrorCode = mapRemoteErrorCode.mapRemoteErrorCode(remoteError.code());
                throw new AppException(internalErrorCode);
            } else {
                log.info("lỗi inventory");
                throw new AppException(ErrorCode.ACCESS_ERROR);
            }
        }
        var orderItemSet = ordersMapper.toOrderItemRequests(itemRequests);
        GhnFeeResponse feeResponse = calculatorFee.join();
        BigDecimal subtotalAmount = calculateSubtotalAmountReview(
                orderItemSet,
                feeResponse.getData().getTotal());

        order.setSubtotalAmount(subtotalAmount);
        order.setShippingCost(BigDecimal.valueOf(Long.parseLong(feeResponse.getData().getTotal())));
        order.setFinalAmount(order.getSubtotalAmount()
                .subtract(order.getDiscountAmount())
                .add(order.getShippingCost())
                .add(order.getTaxAmount())
                .setScale(2, RoundingMode.HALF_UP));
        order = orderInternalService.saveOrder(order, request.getShippingAddressRequest(), itemRequests);
        var orderResponse = ordersMapper.toOrderCreationResponse(order);
        switch (request.getMethod()){
            case BANK_TRANSFER:
                String url = handleStripePayment(itemRequests, order, request.getMethod());
                orderResponse.setSessionUrl(url);
                outEventToShipmentService(
                        itemRequests,
                        orderId,
                        request.getCarrierId(),
                        request.getUserAddressId(),
                        request.getShopPickupPointId(),
                        BigDecimal.ZERO);
                break;
            case CASH:
                handleCashPayment(order, request.getMethod());
                outEventToShipmentService(
                        itemRequests,
                        orderId,
                        request.getCarrierId(),
                        request.getUserAddressId(),
                        request.getShopPickupPointId(),
                        order.getFinalAmount().subtract(order.getShippingCost()));
                break;
        }
        return orderResponse;
    }

    // Lấy thông tin chi tiết order
    public OrderDetailResponse getDetailOrder(String orderId) {
        var order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        return ordersMapper.toOrderDetailResponse(order);
    }

    // Lấy danh sách order theo userId
    public PageResponse<OrderSummaryResponse> getOrderByUserId(int page,
                                                               int size,
                                                               OrderStatus status,
                                                               String fromDate,
                                                               String toDate) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Instant startDate = convertDateService.convertToStartOfDayInstant(fromDate);
        Instant endDate = convertDateService.convertToEndOfDayInstant(toDate);
        Pageable pageable = PageRequest.of(page - 1, size); // Khởi tạo đối tượng phân trang

        var pageData = ordersRepository.findByFilter(status, userId, startDate, endDate, pageable);

        var orderList = ordersMapper.toOrdersResponseList(pageData.getContent());

        return PageResponse.<OrderSummaryResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(orderList)
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
        if(order.getStatus() == OrderStatus.CANCELLING) throw new AppException(ErrorCode.ORDER_BEING_PROCESSED);

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

    // Xem trước đơn hàng, trước khi đơn hàng được tạo
    public OrderReviewResponse review(OrderCreationRequest request) {
        var order = ordersMapper.toOrders(request);
        GhnFeeResponse feeResponse = shipmentClient.calculatorFee(CalculatorFeeRequest.builder()
                .carrierId(request.getCarrierId())
                .shopPickupPointId(request.getShopPickupPointId())
                .userAddressId(request.getUserAddressId())
                .width(0)
                .weight(0)
                .height(0)
                .length(0)
                .insuranceValue(0)
                .build());
        BigDecimal subtotalAmount = calculateSubtotalAmountReview(
                request.getOrderItemRequest(),
                feeResponse.getData().getTotal());

        order.setSubtotalAmount(subtotalAmount);
        order.setFinalAmount(order.getSubtotalAmount()
                .subtract(order.getDiscountAmount())
                .add(order.getShippingCost())
                .add(order.getTaxAmount())
                .setScale(2, RoundingMode.HALF_UP));
        return ordersMapper.toOrderReviewResponse(order);
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
    private String handleStripePayment(Set<ItemRequest> itemRequests, Orders order, MethodType methodType){
        // build dữ liệu gửi qua payment service
        Set<ProductCheckoutRequest> productCheckoutRequests = mapperService.toProductCheckoutRequests(itemRequests);
        TransactionCreationRequest transactionCreationRequest = TransactionCreationRequest.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .method(methodType)
                .amount(order.getFinalAmount())
                .products(productCheckoutRequests)
                .build();
        try {
            var response = paymentClient.createTransaction(transactionCreationRequest);
            var paymentResponseData = response.getResult();
            return paymentResponseData.getSessionUrl();
        } catch (Exception e) {
            // gửi event lên kafka để hủy đơn hàng vừa đặt
            kafkaTemplate.send("order-service.payment.transaction.error",
                    ReleaseEvent.builder()
                            .orderId(order.getId())
                            .build());
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

    private void handleCashPayment(Orders order, MethodType methodType){
        // build dữ liệu gửi qua payment service
        CashPaymentEvent cashPaymentEvent = CashPaymentEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .method(methodType)
                .amount(order.getFinalAmount())
                .build();
        kafkaTemplate.send("order-service.order.cash-payment", cashPaymentEvent);
    }

    private void outEventToShipmentService(
            Set<ItemRequest> itemRequests,
            String orderId,
            String carrierId,
            String userAddressId,
            String shopPickupPointId,
            BigDecimal codeAmount){
        List<GhnItem> ghnItems = itemRequests.stream()
                .map(i -> {
                    return GhnItem.builder()
                            .name(i.getProductNameSnapshot())
                            .price(i.getUnitPriceSnapshot().intValue())
                            .quantity((int) i.getQuantity())
                            .build();
                })

                .toList();
        ShipmentCreationEvent shipmentEvent = ShipmentCreationEvent.builder()
                .orderId(orderId)
                .carrierId(carrierId)
                .codAmount(codeAmount)
                .shopPickupPointId(shopPickupPointId)
                .userAddressId(userAddressId)
                .items(ghnItems)
                .build();
        kafkaTemplate.send("order-service.order.create", shipmentEvent);
    }
}