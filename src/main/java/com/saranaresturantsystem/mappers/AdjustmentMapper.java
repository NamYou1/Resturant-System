package com.saranaresturantsystem.mappers;


import com.saranaresturantsystem.dto.request.AdjustmentRequest;
import com.saranaresturantsystem.dto.response.AdjustmentItemResponse;
import com.saranaresturantsystem.dto.response.AdjustmentResponse;
import com.saranaresturantsystem.entities.Adjustment;
import com.saranaresturantsystem.entities.AdjustmentItem;
import com.saranaresturantsystem.services.StoreService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {StoreService.class})
public interface AdjustmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", source = "storeId")
    Adjustment toEntity(AdjustmentRequest request);

    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "items", source = "items")
    AdjustmentResponse toResponse(Adjustment entity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "unit", source = "unit.name")
    @Mapping(target = "cost", source = "realUnitCost")
    @Mapping(target = "subTotal", source = "subtotal")
    AdjustmentItemResponse toItemResponse(AdjustmentItem item);

}
