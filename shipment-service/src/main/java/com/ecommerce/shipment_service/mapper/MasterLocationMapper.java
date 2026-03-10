package com.ecommerce.shipment_service.mapper;

import com.ecommerce.shipment_service.dto.request.ProvinceClientItemRequest;
import com.ecommerce.shipment_service.dto.response.DistrictClientItemResponse;
import com.ecommerce.shipment_service.dto.response.MasterLocationResponse;
import com.ecommerce.shipment_service.dto.response.WardClientItemResponse;
import com.ecommerce.shipment_service.entity.MasterLocation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MasterLocationMapper {
    MasterLocation toMasterLocation(ProvinceClientItemRequest request);
    MasterLocationResponse toProvinceResponse(MasterLocation masterLocation);
    List<MasterLocationResponse> toMasterLocationResponses(List<MasterLocation> masterLocations);

    List<MasterLocation> toMasterLocationList(List<ProvinceClientItemRequest> requests);

    List<MasterLocation> districtTMasterLocationList(List<DistrictClientItemResponse> responses);
    List<MasterLocation> wardTMasterLocationList(List<WardClientItemResponse> responses);
}