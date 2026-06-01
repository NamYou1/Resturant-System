package com.saranaresturantsystem.controllers.purchases;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.purchases.SupplierRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.purchases.SupplierResponse;
import com.saranaresturantsystem.services.SupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/suppliers")
@Tag(name = "Supplier", description = "Endpoints for managing suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    /**
     * Get all suppliers with pagination
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(supplierService.getListSupplier(params), "Supplier");
    }

    /**
     * Get supplier by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(supplierService.getSupplierById(id), Message.getById("Supplier", id));
    }

    /**
     * Create new supplier
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SupplierResponse>> create(@Valid @ModelAttribute SupplierRequest request) {
        return ResponseFactory.created(supplierService.createSupplier(request), "Supplier");
    }

    /**
     * Update existing supplier
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<SupplierResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute SupplierRequest request) {
        return ResponseFactory.ok(supplierService.updateSupplier(id, request), Message.updated("Supplier", id));
    }

    /**
     * Delete supplier
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseFactory.deleted("Supplier", id);
    }
}
