package com.ecommerce.shipment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class Carriers {
    @Id
    String id;  // GHN
    String name; // Tên đơn vị vận chuyển
    @Builder.Default
    Boolean isActive = true; // trạng thái hoạt động
    Instant createdAt;
    Instant updateAt;
}