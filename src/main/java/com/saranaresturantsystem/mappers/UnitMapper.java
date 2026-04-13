package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.UnitRequest;
import com.saranaresturantsystem.dto.response.UnitResponse;
import com.saranaresturantsystem.entities.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UnitMapper {
    UnitResponse toUnitResponse(Unit unit);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleteFlag", constant = "0")
    Unit toUnit(UnitRequest request);
    @Mapping(target = "id", ignore = true)
    void updateUnitFromRequest(UnitRequest request, @MappingTarget Unit unit);
}