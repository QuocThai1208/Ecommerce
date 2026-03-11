package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.request.ItemBatchDetailRequest;
import com.ecommerce.catalog_service.dto.request.ProductVariantRequest;
import com.ecommerce.catalog_service.dto.request.ProductVariantUpdateRequest;
import com.ecommerce.catalog_service.dto.response.ItemBatchDetailResponse;
import com.ecommerce.catalog_service.dto.response.MultipleFileResponse;
import com.ecommerce.catalog_service.dto.response.ProductVariantResponse;
import com.ecommerce.catalog_service.entity.AttributeValue;
import com.ecommerce.catalog_service.entity.Product;
import com.ecommerce.catalog_service.entity.ProductMedia;
import com.ecommerce.catalog_service.entity.ProductVariant;
import com.ecommerce.catalog_service.enums.ProductStatus;
import com.ecommerce.catalog_service.exception.AppException;
import com.ecommerce.catalog_service.exception.ErrorCode;
import com.ecommerce.catalog_service.mapper.ProductVariantMapper;
import com.ecommerce.catalog_service.repository.AttributeValueRepository;
import com.ecommerce.catalog_service.repository.ProductMediaRepository;
import com.ecommerce.catalog_service.repository.ProductRepository;
import com.ecommerce.catalog_service.repository.ProductVariantRepository;
import com.ecommerce.catalog_service.repository.httpClient.FileClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantService {
    ProductVariantRepository productVariantRepository;
    ProductRepository productRepository;
    AttributeValueRepository attributeValueRepository;
    ProductMediaRepository productMediaRepository;
    ProductVariantMapper productVariantMapper;
    private final FileClient fileClient;
    ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public List<ProductVariantResponse> createProductVariant(List<ProductVariantRequest> request, MultipartFile[] files) {

        var now = Instant.now();

        // 1. Lấy thông tin Product chung
        String firstProductId = request.get(0).getProductId();
        Product product = productRepository.findById(firstProductId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // 2. Map file mảng vào Map để truy xuất nhanh theo tên file
        // Key: Tên file gốc, Value: Đối tượng file
        Map<String, MultipartFile> fileMap = (files != null) ? Arrays.stream(files)
                .collect(Collectors.toMap(MultipartFile::getOriginalFilename, f -> f, (existing, replacement) -> existing))
                : Collections.emptyMap();

        // 3. Khởi tạo danh sách Variant và chuẩn bị dữ liệu upload
        List<MultipartFile> filesToUpload = new ArrayList<>();
        List<String> refIdsForUpload = new ArrayList<>();

        List<ProductVariant> variantToSave = request.stream().map(r -> {
            ProductVariant variant = productVariantMapper.toProductVariant(r);

            // Lấy AttributeValues
            Set<AttributeValue> attributes = StreamSupport.stream(
                    attributeValueRepository.findAllById(r.getAttributeValueIds()).spliterator(), false
            ).collect(Collectors.toSet());

            // Tạo SKU duy nhất (Dùng làm RefId để File Service trả về đúng link)
            String attrCodes = attributes.stream()
                    .map(AttributeValue::getValueCode)
                    .collect(Collectors.joining("-"));
            String shortId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String sku = "VAR-" + shortId + "-" + attrCodes;

            variant.setSku(sku);
            variant.setProduct(product);
            variant.setAttributeValues(attributes);
            variant.setCreated_at(now);
            variant.setUpdate_at(now);

            // KHỚP FILE: Nếu request có gửi kèm tên file, ta nhặt file đó ra để chuẩn bị upload
            if (r.getFileName() != null && fileMap.containsKey(r.getFileName())) {
                filesToUpload.add(fileMap.get(r.getFileName()));
                refIdsForUpload.add(sku); // Dùng SKU làm cầu nối với File Service
            }

            return variant;
        }).toList();

        // 4. Gọi File Service (Batch Upload)
        if (!filesToUpload.isEmpty()) {
            try {
                var uploadData = fileClient.uploadMultipleMedia(
                        filesToUpload.toArray(new MultipartFile[0]),
                        refIdsForUpload
                ).getResult();

                // Chuyển kết quả upload thành Map <SKU, URL>
                Map<String, String> skuToUrlMap = uploadData.stream()
                        .collect(Collectors.toMap(MultipleFileResponse::getRefId, MultipleFileResponse::getUrl));

                // 5. Tạo ProductMedia và gán vào Variant
                variantToSave.forEach(variant -> {
                    String url = skuToUrlMap.get(variant.getSku());
                    if (url != null) {
                        ProductMedia media = ProductMedia.builder()
                                .product(product)
                                .mediaUrl(url)
                                .isMain(!productMediaRepository.existsByProductSlugAndIsMainTrue(firstProductId))
                                .sortOrder(productMediaRepository.findMaxSortOrderByProductId(firstProductId) + 1)
                                .created_at(now)
                                .update_at(now)
                                .build();

                        productMediaRepository.save(media);
                        variant.setProductMedia(media);
                    }
                });
            } catch (Exception e) {
                log.error("File Service Error: ", e);
                throw new AppException(ErrorCode.ERROR_AT_FILE_SERVICE);
            }
        }

        // 6. Lưu tất cả và trả về
        return productVariantMapper.toProductVariantResponseList(productVariantRepository.saveAll(variantToSave));
    }

    public ProductVariantResponse getProductVariant(String productVariantId){
        var productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));
        return productVariantMapper.toProductVariantResponse(productVariant);
    }

    public List<ProductVariantResponse> getByProductSlug(String productSlug){
        var variant = productVariantRepository.findAllByProductSlug(productSlug);
        return productVariantMapper.toProductVariantResponseList(variant);
    }

    public List<ProductVariantResponse> getAllProductVariant(){
        var productVariants = productVariantRepository.findAll();
        return productVariantMapper.toProductVariantResponseList(productVariants);
    }

    public ProductVariantResponse updateProductVariant(String productVariantId, ProductVariantUpdateRequest request){
        var productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));

        productVariantMapper.updateProductVariant(productVariant, request);

        // ** tìm attributeValues
        Iterable<AttributeValue> foundValues = attributeValueRepository.findAllById(request.getAttributeValueIds());
        // Chuyển đổi Iterable -> set
        Set<AttributeValue> attributeValueSet = StreamSupport.stream(
                foundValues.spliterator(), // Mở khóa stream từ Iterable
                false // Tắt xử lý song song, sử lý tuần tự
        ).collect(Collectors.toSet());

        // ** tìm product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        productVariant.setAttributeValues(attributeValueSet);
        productVariant.setProduct(product);
        productVariant.setUpdate_at(Instant.now());

        return productVariantMapper.toProductVariantResponse(productVariantRepository.save(productVariant));
    }

    public List<ItemBatchDetailResponse> getBatchDetails(ItemBatchDetailRequest request) {
        var productVariants = productVariantRepository.findAllById(request.getProductVariantIds());
        return productVariants.stream().map(
                pv -> ItemBatchDetailResponse.builder()
                        .productVariantId(pv.getSku())
                        .productNameSnapshot(pv.getProduct().getName())
                        .unitPriceSnapshot(BigDecimal.valueOf(pv.getPriceAdjustment()))
                        .build()
        ).toList();
    }

    public ProductStatus updateVisibility(String variantSku, String action){
        var variant = productVariantRepository.findById(variantSku)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // Lấy trạng thái hiện tại
        ProductStatus currentStatus = variant.getStatus();

        // Xử lý tín hiệu ẨN (HIDE)
        if ("HIDE".equalsIgnoreCase(action)) {
            // Kiểm tra điều kiện: Không phải DISCONTINUED mới được ẩn
            if (currentStatus == ProductStatus.DISCONTINUED) {
                throw new AppException(ErrorCode.CANNOT_HIDE_DISCONTINUED_PRODUCT);
            }
            // Lưu trạng thái hiện tại vào previousStatus trước khi ghi đè
            variant.setPreviousStatus(currentStatus);
            variant.setStatus(ProductStatus.HIDDEN);
        }

        // Xử lý tín hiệu HIỆN LẠI (SHOW)
        else if ("SHOW".equalsIgnoreCase(action)) {
            if (currentStatus == ProductStatus.HIDDEN) {
                // Khôi phục về trạng thái cũ đã lưu, nếu null thì mặc định ACTIVE
                ProductStatus restoreStatus = (variant.getPreviousStatus() != null)
                        ? variant.getPreviousStatus()
                        : ProductStatus.ACTIVE;

                variant.setStatus(restoreStatus);
                // Sau khi hiện, có thể reset previousStatus về null để sạch sẽ (tùy chọn)
                variant.setPreviousStatus(null);
            }
        }

        variant.setUpdate_at(Instant.now());
        variant = productVariantRepository.save(variant);
        return variant.getStatus();
    }

    @Transactional
    public void updateStatus(String variantId, ProductStatus status){
        var now = Instant.now();
        var variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));
        variant.setStatus(status);
        variant.setUpdate_at(now);
        productVariantRepository.save(variant);

        var product = variant.getProduct();
        if(product.getStatus() == ProductStatus.INACTIVE){
            product.setStatus(ProductStatus.ACTIVE);
            productRepository.save(product);
        }
    }
}