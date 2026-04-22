package com.saranaresturantsystem.Mappers;

import com.saranaresturantsystem.DTO.Request.PurchaseItemRequest;
import com.saranaresturantsystem.DTO.Request.PurchaseRequest;
import com.saranaresturantsystem.DTO.Response.PurchaseItemResponse;
import com.saranaresturantsystem.DTO.Response.PurchaseResponse;
import com.saranaresturantsystem.Enities.Purchase;
import com.saranaresturantsystem.Enities.PurchaseItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "items", ignore = true)
    Purchase toEntity(PurchaseRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchase", ignore = true)
    PurchaseItem toItemEntity(PurchaseItemRequest request);

    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "sellerName", source = "seller.name")
    PurchaseResponse toResponse(Purchase entity);

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "unitName", source = "unit.name")
    PurchaseItemResponse toItemResponse(PurchaseItem entity);

    List<PurchaseItemResponse> toItemResponseList(List<PurchaseItem> items);
}