package com.saranaresturantsystem.Controllers.People;

import com.saranaresturantsystem.DTO.PageDTO;
import com.saranaresturantsystem.DTO.Request.SupplierRequest;
import com.saranaresturantsystem.DTO.Response.ApiResponse;
import com.saranaresturantsystem.DTO.Response.SupplierResponse;
import com.saranaresturantsystem.Services.SupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/suppliers")
@Tag(name = "Supplier", description = "Endpoints for managing suppliers")
public class SupplierController {
    private final  SupplierService supplierService;
    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getListSupplier(
            @RequestParam Map<String , String> params
    ) {
        Page<SupplierResponse> supplierPage = supplierService.getListSupplier(params);
        PageDTO pageDTO = new PageDTO(supplierPage);

        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Suppliers retrieved successfully")
                .payload(pageDTO)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Get category by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierResponse>> getCategoryById(@PathVariable Long id) {
        SupplierResponse supplierResponse = supplierService.getSupplierById(id);
        ApiResponse<SupplierResponse> response = ApiResponse.<SupplierResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Category retrieved successfully")
                .payload(supplierResponse)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Create new category
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SupplierResponse>> createCategory(@Valid @ModelAttribute SupplierRequest request) {
        SupplierResponse response = supplierService.createSupplier(request);
        ApiResponse<SupplierResponse> apiResponse = ApiResponse.<SupplierResponse>builder()
                .succeess(true)
                .status(HttpStatus.CREATED)
                .message("Suppliers created successfully")
                .payload(response)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * Update existing category
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<SupplierResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute SupplierRequest request) {
        SupplierResponse response = supplierService.updateSupplier(id, request);
        ApiResponse<SupplierResponse> apiResponse = ApiResponse.<SupplierResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Supplier updated successfully")
                .payload(response)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Delete category
     */
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        ApiResponse<Void>
                response = ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Category deleted successfully")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
