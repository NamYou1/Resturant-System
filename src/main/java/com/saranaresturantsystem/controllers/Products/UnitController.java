package com.saranaresturantsystem.controllers.Products;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.product.UnitRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.product.UnitResponse;
import com.saranaresturantsystem.services.UnitServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Unit", description = "Endpoints for managing units of measurement")
@RequestMapping("/api/v1/units")
public class UnitController {

    private final UnitServices unitServices;

    @GetMapping
    @PreAuthorize("hasAuthority('unit:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(unitServices.getAllUnits(params), "Unit");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('unit:read')")
    public ResponseEntity<ApiResponse<UnitResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(unitServices.findById(id), Message.getById("Unit", id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('unit:create')")
    public ResponseEntity<ApiResponse<UnitResponse>> create(@Valid @RequestBody UnitRequest request) {
        return ResponseFactory.created(unitServices.createUnit(request), "Unit");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('unit:update')")
    public ResponseEntity<ApiResponse<UnitResponse>> update(@PathVariable Long id, @Valid @RequestBody UnitRequest request) {
        return ResponseFactory.ok(unitServices.updateUnit(id, request), Message.updated("Unit", id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('unit:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        unitServices.deleteUnit(id);
        return ResponseFactory.deleted("Unit", id);
    }
}