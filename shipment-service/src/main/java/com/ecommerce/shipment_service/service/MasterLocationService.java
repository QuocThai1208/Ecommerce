package com.ecommerce.shipment_service.service;

import com.ecommerce.shipment_service.dto.request.ProvinceClientItemRequest;
import com.ecommerce.shipment_service.dto.response.MasterLocationResponse;
import com.ecommerce.shipment_service.dto.response.PageResponse;
import com.ecommerce.shipment_service.enums.LocationType;
import com.ecommerce.shipment_service.exception.AppException;
import com.ecommerce.shipment_service.exception.ErrorCode;
import com.ecommerce.shipment_service.mapper.MasterLocationMapper;
import com.ecommerce.shipment_service.repository.MasterLocationRepository;
import com.ecommerce.shipment_service.repository.httpClient.LocationClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class MasterLocationService {
    MasterLocationRepository masterLocationRepository;
    MasterLocationMapper masterLocationMapper;

    LocationClient locationClient;

    public PageResponse<MasterLocationResponse> getProvince(int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);
        var pageResponse = masterLocationRepository.findAllByType(LocationType.PROVINCE, pageable);
        var provinceResponseList = pageResponse.getContent().stream()
                .map(masterLocationMapper::toProvinceResponse) // map masterLocation -> ProvinceResponse
                .toList();
        return PageResponse.<MasterLocationResponse>builder()
                .currentPage(page)
                .pageSize(pageResponse.getSize())
                .totalPage(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .data(provinceResponseList)
                .build();
    }

    public PageResponse<MasterLocationResponse> getDistrict(int page, int size, String codename){
        var province = masterLocationRepository.findById(codename)
                .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);
        var pageResponse = masterLocationRepository.findAllByParentCode(province, pageable);
        var districtResponse = pageResponse.getContent().stream()
                .map(masterLocationMapper::toProvinceResponse)
                .toList();


        return PageResponse.<MasterLocationResponse>builder()
                .currentPage(page)
                .pageSize(pageResponse.getSize())
                .totalPage(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .data(districtResponse)
                .build();
    }

    public PageResponse<MasterLocationResponse> getWard(int page, int size, String codename){
        var district = masterLocationRepository.findById(codename)
                .orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);
        var pageResponse = masterLocationRepository.findAllByParentCode(district, pageable);
        var wardResponse = pageResponse.getContent().stream()
                .map(masterLocationMapper::toProvinceResponse)
                .toList();

        return PageResponse.<MasterLocationResponse>builder()
                .currentPage(page)
                .pageSize(pageResponse.getSize())
                .totalPage(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .data(wardResponse)
                .build();
    }

    @Transactional
    public String addLocationProvince(List<ProvinceClientItemRequest> itemRequests){
        var masterLocationProvince = masterLocationMapper.toMasterLocationList(itemRequests);
        masterLocationProvince.forEach(location -> {
            location.setCodename(location.getCode()+"_"+location.getCodename());
            location.setType(LocationType.PROVINCE);
        });
        masterLocationRepository.saveAll(masterLocationProvince);
        return "Thêm danh sách tỉnh thành công.";
    }

    @Transactional
    public String addLocationDistrict(){
        var provinces = masterLocationRepository.findAllByType(LocationType.PROVINCE);
        provinces.forEach(province -> {
            var response = locationClient.getDistrict(province.getCode(), 2);

            var districts = response.getDistricts();
            var masterDistrict = masterLocationMapper.districtTMasterLocationList(districts);
            masterDistrict.forEach(d -> {
                d.setCodename(d.getCode()+"_"+d.getCodename());
                d.setType(LocationType.DISTRICT);
                d.setParentCode(province);
            });
            masterLocationRepository.saveAll(masterDistrict);
        });
        return "Thêm danh sách huyện thành công.";
    }

    @Transactional
    public String addLocationWard(){
        var districts = masterLocationRepository.findAllByType(LocationType.DISTRICT);
        for(var district : districts){
            try{
                var response = locationClient.getWare(district.getCode(), 2);
                if (response == null || response.getWards() == null) {
                    continue;
                }
                var wards = response.getWards();
                var masterWard = masterLocationMapper.wardTMasterLocationList(wards);
                masterWard.forEach(d -> {
                    d.setCodename(d.getCode()+"_"+d.getCodename());
                    d.setType(LocationType.WARD);
                    d.setParentCode(district);
                });
                masterLocationRepository.saveAll(masterWard);
            }catch(Exception e){
                continue;
            }
        };
        return "Thêm danh sách xã thành công.";
    }
}