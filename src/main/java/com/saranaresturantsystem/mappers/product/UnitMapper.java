package com.saranaresturantsystem.mappers.product;

import com.saranaresturantsystem.dto.request.product.UnitRequest;
import com.saranaresturantsystem.dto.response.product.UnitResponse;
import com.saranaresturantsystem.entities.product.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UnitMapper {
    UnitResponse toUnitResponse(Unit unit);
    @Mapping(target = "id", ignore = true)
    Unit toUnit(UnitRequest request);
    @Mapping(target = "id", ignore = true)
    void updateUnitFromRequest(UnitRequest request, @MappingTarget Unit unit);
}