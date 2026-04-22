package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.TableRequest;
import com.saranaresturantsystem.dto.response.TableResponse;
import com.saranaresturantsystem.entities.Tables;
import com.saranaresturantsystem.services.GroupService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {GroupService.class})
public interface TableMapper {
    // Entity -> Response DTO
    @Mapping(target = "id", source = "tableId")
//    @Mapping(target = "group", source = "tableGroup")
    @Mapping(target = "groupId", source = "tableGroup.id")
    @Mapping(target = "groupName", source = "tableGroup.name")
    TableResponse toResponse(Tables entity);

//    // List of Entity -> List of Response DTO
//    List<TableResponse> toResponseList(List<Tables> entities);

    // Request DTO -> Entity (For Creation)
    @Mapping(target = "tableId", ignore = true)
    @Mapping(target = "tableGroup", source = "groupId") // Handled in Service
    @Mapping(target = "status" , ignore = true)
    Tables toEntity(TableRequest request);

    // Update existing Entity from Request DTO
    @Mapping(target = "tableId", ignore = true)
    @Mapping(target = "tableGroup", source = "groupId") // Handled in Service
    @Mapping(target = "status" , ignore = true)
    void updateEntityFromRequest(TableRequest request, @MappingTarget Tables entity);
}
