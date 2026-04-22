package com.saranaresturantsystem.Mappers;

import com.saranaresturantsystem.DTO.Request.SellerRequest;
import com.saranaresturantsystem.DTO.Response.SellerResponse;
import com.saranaresturantsystem.Enities.Seller;
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