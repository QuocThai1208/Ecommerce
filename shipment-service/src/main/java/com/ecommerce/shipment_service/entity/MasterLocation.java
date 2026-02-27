package com.ecommerce.shipment_service.entity;

import com.ecommerce.shipment_service.enums.LocationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class MasterLocation {
    @Id
    String codename;
    String name; // tên thành phố/quận

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    LocationType type; // cấp của địa chỉ

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "parentCode")
    MasterLocation parentCode; // id của địa chỉ cha
    Long code;  // mã đại diện của địa chi trong hệ thống
}