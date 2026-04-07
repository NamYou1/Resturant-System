package com.saranaresturantsystem.Mappers;

import com.saranaresturantsystem.DTO.Request.SupplierRequest;
import com.saranaresturantsystem.DTO.Response.SupplierResponse;
import com.saranaresturantsystem.Enities.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface   SupplerMapper {

    SupplierResponse toSupplierResponse(Supplier supplier);
    @Mapping(target = "id", ignore = true)
    Supplier toSuppler(SupplierRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEnityFromRequest(SupplierRequest request, @MappingTarget Supplier supplier);

}
