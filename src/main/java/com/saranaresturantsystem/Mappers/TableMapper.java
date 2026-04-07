package com.saranaresturantsystem.Mappers;

import com.saranaresturantsystem.DTO.Request.TableRequest;
import com.saranaresturantsystem.DTO.Response.TableResponse;
import com.saranaresturantsystem.Enities.TableEnities;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper {
    // Entity -> Response DTO
    @Mapping(target = "id", source = "tableId")
    @Mapping(target = "group", source = "tableGroup")
    TableResponse toResponse(TableEnities entity);

    // List of Entity -> List of Response DTO
    List<TableResponse> toResponseList(List<TableEnities> entities);

    // Request DTO -> Entity (For Creation)
    @Mapping(target = "tableId", ignore = true)
    @Mapping(target = "tableGroup", ignore = true) // Handled in Service
    TableEnities toEntity(TableRequest request);

    // Update existing Entity from Request DTO
    @Mapping(target = "tableId", ignore = true)
    @Mapping(target = "tableGroup", ignore = true) // Handled in Service
    void updateEntityFromRequest(TableRequest request, @MappingTarget TableEnities entity);
}
