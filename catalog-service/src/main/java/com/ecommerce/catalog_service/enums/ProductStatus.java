package com.ecommerce.catalog_service.enums;

public enum ProductStatus {
    INACTIVE,       // Chưa nhập hành
    ACTIVE,         // Đang hiển thị và cho phép bán
    OUT_OF_STOCK,   // Hết hàng (tự động hoặc thủ công)
    HIDDEN,         // Tạm ẩn (người bán muốn ẩn nhưng chưa xóa)
    DISCONTINUED,   // Ngừng kinh doanh (không nhập thêm hàng nữa)
}
