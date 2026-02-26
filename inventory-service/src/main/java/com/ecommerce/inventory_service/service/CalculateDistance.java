package com.ecommerce.inventory_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CalculateDistance {
    // Bán kính trung bình của Trái Đất theo km
    static final int EARTH_RADIUS_KM = 6371;

    // Tính khoảng cách giữa hai điểm trên bề mặt Trái Đất sử dụng công thức Haversine
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2 ){
        // Chuyển đổi độ sang radian
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        // Áp dụng phần sin^2 của công thức Haversine
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        // Áp dụng phần acos
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // Trả về khoảng cách tính bằng km
    }
}