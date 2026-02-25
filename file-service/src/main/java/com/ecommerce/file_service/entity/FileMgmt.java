package com.ecommerce.file_service.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(value = "file_mgmt")
public class FileMgmt {
    @MongoId
    String id;
    String ownerId; // Chử sỡ hữu
    String contentType;
    String url;
    String version;
    String size;
}