package com.saranaresturantsystem.mappers.sales;

import com.saranaresturantsystem.dto.request.sales.OptionRequest;
import com.saranaresturantsystem.dto.response.sales.OptionResponse;
import com.saranaresturantsystem.entities.product.Options;
import com.saranaresturantsystem.services.GroupService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        GroupService.class,
})
public interface OptionsMapper {

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "groupName", source = "group.name")
    OptionResponse toOptionsResponse(Options options);

    @Mapping(target = "group", source = "groupId")
    Options toOptions(OptionRequest request);

    @Mapping(target = "group", source = "groupId")
    void updateOptions(OptionRequest request,
                       @MappingTarget Options options);
}