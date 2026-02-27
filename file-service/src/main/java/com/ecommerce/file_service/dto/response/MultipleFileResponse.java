package com.ecommerce.file_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MultipleFileResponse {
    String originalFileName;
    String url;
    String refId;
}