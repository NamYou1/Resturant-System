package com.saranaresturantsystem.mappers.purchases;

import com.saranaresturantsystem.dto.request.purchases.SupplierRequest;
import com.saranaresturantsystem.dto.response.purchases.SupplierResponse;
import com.saranaresturantsystem.entities.purchases.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface   SupplerMapper {

    SupplierResponse toSupplierResponse(Supplier supplier);
    @Mapping(target = "id", ignore = true)
    Supplier toSuppler(SupplierRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(SupplierRequest request, @MappingTarget Supplier supplier);

}
