package com.ecommerce.inventory_service.type;

import lombok.Getter;

@Getter
public enum TransactionType {
    GOODS_RECEIPT("Nhận hàng từ nhà cung cấp theo Đơn hàng mua"),
    RETURN_FROM_CUSTOMER("Khách hàng hoàn trả sản phẩm (Sau khi đã nhận hàng)."),
    MANUFACTURING_COMPLETION("Nhập kho thành phẩm sau khi quá trình sản xuất hoàn tất."),

    GOODS_ISSUE("Xuất nguyên vật liệu thô để phục vụ quá trình sản xuất."),
    ISSUED_FOR_PRODUCTION("Xuất kho thành phẩm sau khi quá trình sản xuất hoàn tất."),
    SCRAP_OR_DAMAGE("Loại bỏ hàng bị hỏng, hết hạn hoặc thất thoát khỏi kho."),

    RESERVATION("Hàng hóa được đặt trước khi khách hàng tạo đơn hàng."),
    RESERVATION_RELEASE("Hủy đặt trước."),
    ;

    TransactionType(String description) {
        this.description = description;
    }

    private String description;
}