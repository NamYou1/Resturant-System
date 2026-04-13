package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.SupplierRequest;
import com.saranaresturantsystem.dto.response.SupplierResponse;
import com.saranaresturantsystem.entities.Supplier;
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
