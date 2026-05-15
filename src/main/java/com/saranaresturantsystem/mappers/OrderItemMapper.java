package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.OrderItemRequest;
import com.saranaresturantsystem.dto.response.OrderItemResponse;
import com.saranaresturantsystem.entities.OrderItem;
import com.saranaresturantsystem.services.TableService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TableService.class})
public interface OrderItemMapper {

    @Mapping(target = "tableId", source = "tables.tableId")
    @Mapping(target = "tableName", source = "tables.name")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId",ignore = true)
    @Mapping(target = "itemId",ignore = true)
    @Mapping(target = "tables", source = "tableId")
    @Mapping(target = "date", ignore = true)
    OrderItem toOrderItem(OrderItemRequest orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId",ignore = true)
    @Mapping(target = "itemId",ignore = true)
    @Mapping(target = "tables", source = "tableId")
    @Mapping(target = "date", ignore = true)
    void updateOrderItemFromResponse(
            OrderItemRequest request,
            @MappingTarget OrderItem orderItem
    );
}
