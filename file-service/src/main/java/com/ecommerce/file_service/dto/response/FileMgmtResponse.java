package com.ecommerce.file_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileMgmtResponse {
    String id;
    String ownerId; // Chử sỡ hữu
    String contentType;
    String url;
    String version;
    String size;
}