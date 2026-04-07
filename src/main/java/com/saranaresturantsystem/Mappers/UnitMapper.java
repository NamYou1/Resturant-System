package com.saranaresturantsystem.Mappers;

import com.saranaresturantsystem.DTO.Request.UnitRequest;
import com.saranaresturantsystem.DTO.Response.UnitResponse;
import com.saranaresturantsystem.Enities.Unit;
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