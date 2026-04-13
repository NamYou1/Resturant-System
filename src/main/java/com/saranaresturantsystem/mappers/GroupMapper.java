package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.GroupRequest;
import com.saranaresturantsystem.dto.response.GroupResponse;
import com.saranaresturantsystem.entities.Group;
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
    Group toEntity(GroupRequest request);

    // 4. Update existing Entity from Request DTO
    @Mapping(target = "id", ignore = true) // Don't change the ID during update
    void updateEntityFromRequest(GroupRequest request, @MappingTarget Group entity);
}
