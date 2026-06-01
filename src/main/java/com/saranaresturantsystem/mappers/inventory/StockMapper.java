package com.saranaresturantsystem.mappers.inventory;

import com.saranaresturantsystem.dto.response.inventory.StockResponse;
import com.saranaresturantsystem.entities.inventory.ProductStoreQty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "store.name", target = "storeName")
    StockResponse toResponse(ProductStoreQty stock);
}