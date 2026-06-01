//package com.saranaresturantsystem.mappers;
//
//import com.saranaresturantsystem.dto.request.sales.CustomerRequest;
//import com.saranaresturantsystem.dto.response.sales.CustomerResponse;
//import com.saranaresturantsystem.entities.sales.Customer;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingTarget;
//
//@Mapper(componentModel = "spring")
//public interface CustomerMapper {
//    @Mapping(target = "storeName", source = "store.name")
//    @Mapping(target = "tableGroupName", source = "tableGroup.name")
//    @Mapping(target = "priceGroupName", source = "priceGroupName")
//    CustomerResponse toResponse(Customer customer);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "status", ignore = true)
//    @Mapping(target = "store", ignore = true)
//    @Mapping(target = "tableGroup", ignore = true)
//    @Mapping(target = "tableGroupName", ignore = true)
//    @Mapping(target = "priceGroupName", ignore = true)
//    Customer toEntity(CustomerRequest request);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "status", ignore = true)
//    @Mapping(target = "store", ignore = true)
//    @Mapping(target = "tableGroup", ignore = true)
//    @Mapping(target = "tableGroupName", ignore = true)
//    @Mapping(target = "priceGroupName", ignore = true)
//    void updateEntityFromRequest(CustomerRequest request, @MappingTarget Customer customer);
//}
//
