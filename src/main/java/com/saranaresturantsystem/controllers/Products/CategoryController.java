package com.saranaresturantsystem.controllers.Products;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.product.CategoryRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.product.CategoryResponse;
import com.saranaresturantsystem.services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@Tag(name = "Category", description = "Endpoints for managing categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Get all categories with pagination
     */
    @GetMapping
    @PreAuthorize("hasAuthority('category:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(categoryService.getListCategory(params), "Category");
    }

    /**
     * Get category by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('category:read')")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(categoryService.getCategoryById(id), Message.getById("Category", id));
    }

    /**
     * Create new category
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('category:create')")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @ModelAttribute CategoryRequest request) {
        return ResponseFactory.created(categoryService.createCategory(request), "Category");
    }

    /**
     * Update existing category
     */
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('category:update')")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute CategoryRequest request) {
        return ResponseFactory.ok(categoryService.updateCategory(id, request), Message.updated("Category", id));
    }

    /**
     * Delete category
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('category:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseFactory.deleted("Category", id);
    }
}
