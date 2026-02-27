package com.ecommerce.catalog_service.util;

import java.text.Normalizer;
import java.util.UUID;
import java.util.regex.Pattern;

public class SlugUtils {
    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) return "";

        // 1. Phân tách dấu và xóa dấu
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String out = pattern.matcher(nfdNormalizedString).replaceAll("");

        String shortId = UUID.randomUUID().toString().substring(0, 8);

        // 2. Xử lý chữ Đ, đổi sang chữ thường, thay khoảng trắng bằng gạch ngang
        String slug =  out.toLowerCase()
                .replace('đ', 'd')
                .replace('Đ', 'd')
                .replaceAll("[^a-z0-9\\s]", "") // Xóa ký tự đặc biệt
                .replaceAll("\\s+", "-")        // Thay khoảng trắng bằng -
                .replaceAll("^-+|-+$", "");     // Xóa gạch ngang thừa ở đầu/cuối

        return "product" + shortId + "-" + slug;
    }

    public static String toUnaccentUpperCase(String value) {
        if (value == null) return null;

        // 1. Tách các ký tự dấu ra khỏi chữ cái gốc
        String temp = Normalizer.normalize(value, Normalizer.Form.NFD);

        // 2. Dùng Regex loại bỏ các ký tự dấu (Non-spacing Mark)
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String out = pattern.matcher(temp).replaceAll("");

        // 3. Xử lý riêng chữ Đ (vì Normalizer không nhận diện được chữ này)
        // Sau đó chuyển tất cả sang chữ in hoa
        return out.replace('đ', 'd').replace('Đ', 'D').toUpperCase();
    }
}