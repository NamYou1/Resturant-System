package com.saranaresturantsystem.mappers;
import com.saranaresturantsystem.dto.request.SaleRequest;
import com.saranaresturantsystem.dto.response.SaleResponse;
import com.saranaresturantsystem.entities.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
}