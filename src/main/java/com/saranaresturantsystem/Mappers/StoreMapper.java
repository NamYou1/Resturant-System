package com.saranaresturantsystem.Mappers;

import com.saranaresturantsystem.DTO.Request.StoreRequest;
import com.saranaresturantsystem.DTO.Response.StoreResponse;
import com.saranaresturantsystem.Enities.Store;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    Store toEntity(StoreRequest request);

    StoreResponse toResponse(Store store);

    List<StoreResponse> toResponseList(List<Store> stores);

    void updateEntityFromRequest(StoreRequest request, @MappingTarget Store store);
}