package com.saranaresturantsystem.controllers.users;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.request.users.RoleRequest;
import com.saranaresturantsystem.dto.response.users.RoleResponse;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.services.users.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
@Tag(name = "Role Controller", description = "APIs for managing roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('role:read')")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
        List<RoleResponse> payload = roleService.getAll();
        return ResponseFactory.ok(payload, Message.getAll("Role"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:read')")
    public ResponseEntity<ApiResponse<RoleResponse>> getById(@PathVariable Long id) {
        RoleResponse payload = roleService.getById(id);
        return ResponseFactory.ok(payload, Message.getById("Role", id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:create')")
    public ResponseEntity<ApiResponse<RoleResponse>> create(@Valid @RequestBody RoleRequest request) {
        RoleResponse payload = roleService.create(request);
        return ResponseFactory.created(payload, "Role");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<ApiResponse<RoleResponse>> update(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        RoleResponse payload = roleService.update(id, request);
        return ResponseFactory.ok(payload, Message.updated("Role", id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseFactory.deleted("Role", id);
    }
}
