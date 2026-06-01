package com.saranaresturantsystem.mappers.users;

import com.saranaresturantsystem.dto.request.users.PermissionRequest;
import com.saranaresturantsystem.dto.response.users.PermissionResponse;
import com.saranaresturantsystem.entities.users.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    List<PermissionResponse> toListPermissionResponse(List<Permission> permissions);

    PermissionResponse toResponse(Permission permission);

    Permission toEntity(PermissionRequest request);

    void updateFromRequest(PermissionRequest request, @MappingTarget Permission permission);
}
