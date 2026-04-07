package com.saranaresturantsystem.Mappers;

import com.saranaresturantsystem.DTO.Request.StoreRequest;
import com.saranaresturantsystem.DTO.Response.StoreResponse;
import com.saranaresturantsystem.Enities.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    @Mapping(target = "id", ignore = true)
    Store toEntity(StoreRequest request);

    StoreResponse toResponse(Store store);

    List<StoreResponse> toResponseList(List<Store> stores);
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(StoreRequest request, @MappingTarget Store store);
}