package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.request.ShipmentRequest;
import com.ecommerce.order_service.dto.request.ShipmentUpdateStatusRequest;
import com.ecommerce.order_service.dto.response.ShipmentResponse;
import com.ecommerce.order_service.enums.OrderStatus;
import com.ecommerce.order_service.enums.ShipmentStatus;
import com.ecommerce.order_service.exception.AppException;
import com.ecommerce.order_service.exception.ErrorCode;
import com.ecommerce.order_service.mapper.ShipmentMapper;
import com.ecommerce.order_service.repository.OrdersRepository;
import com.ecommerce.order_service.repository.ShipmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShipmentService {
    ShipmentRepository shipmentRepository;
    OrdersRepository ordersRepository;
    ShipmentMapper shipmentMapper;

    // Được gọi từ shipment service sau khi đã đóng gói hàng xong
    @Transactional
    public ShipmentResponse create(String orderId, ShipmentRequest request){
        if(shipmentRepository.existsByOrderId(orderId)){
            throw new AppException(ErrorCode.SHIPMENT_ALREADY_EXISTED);
        }

        var order = ordersRepository.findById(orderId)
                .orElseThrow(()-> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        var shipment =  shipmentMapper.toShipment(request);
        shipment.setOrder(order);
        shipment.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.PROCESSING); // Sẳn sàng giao hàng
        ordersRepository.save(order);
        return shipmentMapper.toShipmentResponse(shipmentRepository.save(shipment));
    }

    // cập nhật trạng thái vận chuyển
    @Transactional
    public ShipmentResponse updateStatus(String orderId, String shipmentId, ShipmentUpdateStatusRequest request){
        var shipment = shipmentRepository.findByIdAndOrderId(shipmentId, orderId)
                .orElseThrow(()-> new AppException(ErrorCode.SHIPMENT_NOT_FOUND));

        var order = ordersRepository.findById(orderId)
                .orElseThrow(()-> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        shipment.setShipmentStatus(request.getStatus());
        if (shipment.getShipmentStatus() == ShipmentStatus.SHIPPED || shipment.getShipmentStatus() == ShipmentStatus.IN_TRANSIT){
            order.setStatus(OrderStatus.SHIPPED);
        }
        else if(shipment.getShipmentStatus() == ShipmentStatus.DELIVERED){
            order.setStatus(OrderStatus.DELIVERED);
        }
        ordersRepository.save(order);
        return shipmentMapper.toShipmentResponse(shipmentRepository.save(shipment));
    }

    // Lấy danh sách vận chuyển theo đơn hàng
    public List<ShipmentResponse> getByOrderId(String orderId) {
        var shipments = shipmentRepository.findAllByOrderId(orderId);
        return shipmentMapper.toShipmentResponseList(shipments);
    }

    // Lấy chi tiết vận chuyển theo shipmentId
    public ShipmentResponse getById(String shipmentId, String orderId) {
        var shipment = shipmentRepository.findByIdAndOrderId(shipmentId, orderId)
                .orElseThrow(() -> new AppException(ErrorCode.SHIPMENT_NOT_FOUND));
        return shipmentMapper.toShipmentResponse(shipment);
    }
}