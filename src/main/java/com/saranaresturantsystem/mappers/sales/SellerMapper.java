package com.saranaresturantsystem.mappers.sales;

import com.saranaresturantsystem.dto.request.sales.SellerRequest;
import com.saranaresturantsystem.dto.response.sales.SellerResponse;
import com.saranaresturantsystem.entities.sales.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    SellerResponse toSellerResponse(Seller seller);

    @Mapping(target = "id", ignore = true)
    Seller toSeller(SellerRequest request);

    @Mapping(target = "id", ignore = true)
    void updateSellerFromRequest(SellerRequest request, @MappingTarget Seller entity);
}