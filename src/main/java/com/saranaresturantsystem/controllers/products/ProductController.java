package com.saranaresturantsystem.controllers.Products;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.product.ProductRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.product.ProductResponse;
import com.saranaresturantsystem.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Product", description = "Endpoints for managing restaurant products and inventory")
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Get all products with pagination and filters
     */
    @GetMapping
    @Operation(summary = "Get all products with pagination and filters")
    @PreAuthorize("hasAuthority('product:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getList(
            @Parameter(description = "Filter params: page, size, sort, name, code, type, status, categoryId")
            @RequestParam Map<String, String> params) {
        return ResponseFactory.ok(productService.getAllProducts(params), "Product");
    }

    /**
     * Find a product by its ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Find a product by its ID")
    @PreAuthorize("hasAuthority('product:read')")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(productService.findById(id), Message.getById("Product", id));
    }

    /**
     * Create a new product with an optional image
     */
    @PostMapping()
    @Operation(summary = "Create a new product with an optional image")
    @PreAuthorize("hasAuthority('product:create')")
    public ResponseEntity<ApiResponse<ProductResponse>> create(
            @Valid @RequestBody ProductRequest request) {
        return ResponseFactory.created(productService.createProduct(request ), "Product");
    }

    /**
     * Update product details; send a new image file to replace the existing one
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update product details; send a new image file to replace the existing one")
    @PreAuthorize("hasAuthority('product:update')")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductRequest request) {
        return ResponseFactory.ok(productService.updateProduct(id, request), Message.updated("Product", id));
    }

    /**
     * Soft-delete a product by setting its status to INACTIVE
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a product by setting its status to INACTIVE")
    @PreAuthorize("hasAuthority('product:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseFactory.deleted("Product", id);
    }
}