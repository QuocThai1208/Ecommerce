package com.ecommerce.order_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConvertDateService {

    // chuyển string(yyyy-mm-dd) -> instant đại diện cho cuối ngày vd: 2025-12-12:23:59:59
    public Instant convertToEndOfDayInstant(String dateString){
        if(dateString == null || dateString.isEmpty()){
            return null;
        }
        // định nghĩa dạng ngày tháng
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        // Phân tích chuỗi
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate.atTime(LocalTime.MAX) // 23:59:59
                .atZone(ZoneId.systemDefault()) // chuyển sang giờ UTC
                .toInstant();
    }

    // chuyển string(yyyy-mm-dd) -> instant đại diện cho đầu ngày vd: 2025-12-12:00:00:00
    public Instant convertToStartOfDayInstant(String dateString){
        if(dateString == null || dateString.isEmpty()){
            return null;
        }
        // định nghĩa dạng ngày tháng
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        // Phân tích chuỗi
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate.atStartOfDay() // 00:00:00
                .atZone(ZoneId.systemDefault()) // chuyển sang giờ UTC
                .toInstant();
    }
}