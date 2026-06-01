package com.saranaresturantsystem.services.users;

import com.saranaresturantsystem.dto.request.users.RoleRequest;
import com.saranaresturantsystem.dto.response.users.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAll();
    RoleResponse getById(Long id);
    RoleResponse create(RoleRequest request);
    RoleResponse update(Long id, RoleRequest request);
    void delete(Long id);
}
