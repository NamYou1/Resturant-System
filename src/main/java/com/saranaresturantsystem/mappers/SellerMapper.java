package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.SellerRequest;
import com.saranaresturantsystem.dto.response.SellerResponse;
import com.saranaresturantsystem.entities.Seller;
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