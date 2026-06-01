package com.saranaresturantsystem.mappers.sales;

import com.saranaresturantsystem.dto.request.sales.SaleRequest;
import com.saranaresturantsystem.dto.response.sales.SaleResponse;
import com.saranaresturantsystem.entities.sales.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SaleMapper {
    @Mapping(target = "id", ignore      = true)
    @Mapping(target = "items", ignore   = true)
    @Mapping(target = "no", ignore      = true)
    @Mapping(target = "date", ignore    = true)
    @Mapping(target = "holdRef", ignore = true)
    Sale toEntity(SaleRequest request);

    @Mapping(target = "referenceNo", source = "holdRef")
    SaleResponse toResponse(Sale sale);

    @Mapping(target = "id", ignore      = true)
    @Mapping(target = "items", ignore   = true)
    @Mapping(target = "no", ignore      = true)
    @Mapping(target = "date", ignore    = true)
    @Mapping(target = "holdRef", ignore = true)
    void updateFromRequest(SaleRequest request, @MappingTarget Sale sale);
}