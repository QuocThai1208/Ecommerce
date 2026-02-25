package com.ecommerce.file_service.repository;

import com.ecommerce.file_service.entity.FileMgmt;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileMgmtRepository extends MongoRepository<FileMgmt, String> {
}