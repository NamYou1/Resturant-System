package com.saranaresturantsystem.Controllers.Settings;


import com.saranaresturantsystem.DTO.Request.GroupRequest;
import com.saranaresturantsystem.DTO.Response.ApiResponse;
import com.saranaresturantsystem.DTO.Response.GroupResponse;
import com.saranaresturantsystem.Services.GroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
@Tag(name = "Group", description = "Endpoints for managing groups")
public class GroupController {
    private final GroupService groupService;
    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupResponse>>> getAllGroups() {
        return ResponseEntity.ok(ApiResponse.<List<GroupResponse>>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Group loaded successfully")
                .payload(groupService.getAllGroups())
                .timestamp(Instant.now())
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupResponse>> getGroup(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<GroupResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Group retrieved with id= "+id+" successfully")
                .payload(groupService.getGroupById(id))
                .timestamp(Instant.now())
                .build());
    }
    @PostMapping
    public ResponseEntity<ApiResponse<GroupResponse>> createGroup(@Valid @RequestBody GroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<GroupResponse>builder()
                .succeess(true)
                .message("Group created successfully")
                .payload(groupService.createGroup(request))
                .timestamp(Instant.now())
                .build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupResponse>> updateTable(@PathVariable Long id, @Valid @RequestBody GroupRequest request) {
        return ResponseEntity.ok(ApiResponse.<GroupResponse>builder()
                .succeess(true)
                .message("Group updated successfully")
                .payload(groupService.updateGroup(id, request))
                .timestamp(Instant.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTable(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .succeess(true)
                .message("Group deleted successfully")
                .payload(null)
                .timestamp(Instant.now())
                .build());
    }
}
