package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.OptionRequest;
import com.saranaresturantsystem.dto.response.OptionResponse;
import com.saranaresturantsystem.entities.Options;
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