package com.saranaresturantsystem.mappers.inventory;

import com.saranaresturantsystem.dto.request.inventory.TransferItemRequest;
import com.saranaresturantsystem.dto.request.inventory.TransferRequest;
import com.saranaresturantsystem.dto.response.inventory.TransferItemResponse;
import com.saranaresturantsystem.dto.response.inventory.TransferResponse;
import com.saranaresturantsystem.entities.inventory.Transfer;
import com.saranaresturantsystem.entities.inventory.TransferItem;
import com.saranaresturantsystem.services.ProductService;
import com.saranaresturantsystem.services.StoreService;
import com.saranaresturantsystem.services.UnitServices;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StoreService.class , ProductService.class , UnitServices.class})
public interface TransferMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fromStoreId" , source = "fromStoreId")
    @Mapping(target = "toStoreId" , source = "toStoreId")
    Transfer toEntity(TransferRequest request);

//    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fromStoreId", source = "fromStoreId.id")
    @Mapping(target = "toStoreId", source = "toStoreId.id")
    // RESPONSE
    TransferResponse toResponse(Transfer transfer);

//    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId" , source = "product.id")
    @Mapping(target = "unitId" , source = "unit.id")
    TransferItemResponse toItemResponse(TransferItem item);

//    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product" , source = "productId")
    @Mapping(target = "unit"  , source = "unitId")
    TransferItem toItemRequest(TransferItemRequest entity);




    List<TransferResponse> toResponseList(List<Transfer> transfers);

    // UPDATE (only allowed fields)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fromStoreId" , source = "fromStoreId")
    @Mapping(target = "toStoreId" , source = "toStoreId")
    void updateEntity(TransferRequest request, @MappingTarget Transfer transfer);
}