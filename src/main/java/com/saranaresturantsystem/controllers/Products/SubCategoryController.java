package com.saranaresturantsystem.controllers.Products;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.product.SubCategoryRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.product.SubCategoryResponse;
import com.saranaresturantsystem.services.SubCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "SubCategory", description = "Endpoints for managing subcategories")
@RequestMapping("/api/v1/subcategory")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    /**
     * Get all subcategories with pagination
     */
    @GetMapping
    @PreAuthorize("hasAuthority('subCategory:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(subCategoryService.getAllSubCategory(params), "SubCategory");
    }

    /**
     * Get subcategory by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('subCategory:read')")
    public ResponseEntity<ApiResponse<SubCategoryResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(subCategoryService.findById(id), Message.getById("SubCategory", id));
    }

    /**
     * Create subcategory (has-a relationship with category)
     */
    @PostMapping
    @PreAuthorize("hasAuthority('subCategory:create')")
    public ResponseEntity<ApiResponse<SubCategoryResponse>> create(@Valid @RequestBody SubCategoryRequest request) {
        return ResponseFactory.created(subCategoryService.createSubCategory(request), "SubCategory");
    }

    /**
     * Update subcategory
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('subCategory:update')")
    public ResponseEntity<ApiResponse<SubCategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody SubCategoryRequest request) {
        return ResponseFactory.ok(subCategoryService.updateSubCategory(id, request), Message.updated("SubCategory", id));
    }

    /**
     * Delete subcategory (soft-delete: sets status from active to inactive)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('subCategory:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
        return ResponseFactory.deleted("SubCategory", id);
    }
}
