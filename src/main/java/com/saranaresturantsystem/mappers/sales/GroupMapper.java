package com.saranaresturantsystem.mappers.sales;

import com.saranaresturantsystem.dto.request.sales.GroupRequest;
import com.saranaresturantsystem.dto.response.sales.GroupResponse;
import com.saranaresturantsystem.entities.sales.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    // 1. Convert Entity to Response DTO
    GroupResponse toResponse(Group entity);

    // 2. Convert List of Entities to List of Response DTOs
    List<GroupResponse> toResponseList(List<Group> entities);

    // 3. Convert Request DTO to Entity (for creation)
    @Mapping(target = "id", ignore = true) // ID is auto-generated
    @Mapping(target = "tables" , ignore = true)
    Group toEntity(GroupRequest request);

    // 4. Update existing Entity from Request DTO
    @Mapping(target = "id", ignore = true) // Don't change the ID during update
    @Mapping(target = "tables" , ignore = true)

    void updateEntityFromRequest(GroupRequest request, @MappingTarget Group entity);
}
