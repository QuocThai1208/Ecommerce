package com.ecommerce.shipment_service.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class CarrierLocationMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localCode", nullable = false)
    MasterLocation masterLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrierId", nullable = false)
    Carriers carriers; // đơn vị vận chuyểm

    String carrierValue; // giá trị của đơn vi vận chuyển
}