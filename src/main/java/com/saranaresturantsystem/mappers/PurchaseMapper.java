package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.PurchaseItemRequest;
import com.saranaresturantsystem.dto.request.PurchaseRequest;
import com.saranaresturantsystem.dto.response.PurchaseItemResponse;
import com.saranaresturantsystem.dto.response.PurchaseResponse;
import com.saranaresturantsystem.entities.Purchase;
import com.saranaresturantsystem.entities.PurchaseItem;
import com.saranaresturantsystem.entities.Supplier;
import com.saranaresturantsystem.services.SellerService;
import com.saranaresturantsystem.services.StoreService;
import com.saranaresturantsystem.services.SupplierService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {SupplierService.class , SellerService.class , StoreService.class})
public interface PurchaseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "supplier", source = "supplierId")
    @Mapping(target = "seller", source = "sellerId")
    @Mapping(target = "store", source = "storeId")
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
    @Mapping(target = "productId" , source = "product.id")
    PurchaseItemResponse toItemResponse(PurchaseItem entity);

    List<PurchaseItemResponse> toItemResponseList(List<PurchaseItem> items);
}