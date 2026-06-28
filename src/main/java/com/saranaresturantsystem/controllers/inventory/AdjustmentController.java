package com.saranaresturantsystem.controllers.inventory;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.inventory.AdjustmentRequest;
import com.saranaresturantsystem.dto.response.inventory.AdjustmentResponse;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.services.AdjustmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/adjustment")
@Tag(name = "Adjustment", description = "Endpoints for managing adjustments")
public class AdjustmentController {

    private final AdjustmentService adjustmentService;

    @GetMapping
    @PreAuthorize("hasAuthority('adjustment:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(adjustmentService.getList(params), "Adjustment");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('adjustment:read')")
    public ResponseEntity<ApiResponse<AdjustmentResponse>> getById(@Valid @PathVariable Long id) {
        return ResponseFactory.ok(adjustmentService.findById(id), Message.getById("Adjustment", id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('adjustment:create')")
    public ResponseEntity<ApiResponse<AdjustmentResponse>> create(@Valid @RequestBody AdjustmentRequest request) {
        return ResponseFactory.created(adjustmentService.createAdjustment(request), "Adjustment");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('adjustment:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adjustmentService.deleteAdjustment(id);
        return ResponseFactory.deleted("Adjustment", id);
    }
}
