package com.saranaresturantsystem.controllers.sales;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.sales.GroupRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.sales.GroupResponse;
import com.saranaresturantsystem.services.GroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
@Tag(name = "Group", description = "Endpoints for managing groups")
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    @PreAuthorize("hasAuthority('group:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(groupService.getAllGroups(params), "Group");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('group:read')")
    public ResponseEntity<ApiResponse<GroupResponse>> getById(@Positive @PathVariable Long id) {
        return ResponseFactory.ok(groupService.getGroupById(id), Message.getById("Group", id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('group:create')")
    public ResponseEntity<ApiResponse<GroupResponse>> create(@Valid @RequestBody GroupRequest request) {
        return ResponseFactory.created(groupService.createGroup(request), "Group");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('group:update')")
    public ResponseEntity<ApiResponse<GroupResponse>> update(@PathVariable Long id, @Valid @RequestBody GroupRequest request) {
        return ResponseFactory.ok(groupService.updateGroup(id, request), Message.updated("Group", id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('group:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseFactory.deleted("Group", id);
    }
}
