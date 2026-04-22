package com.saranaresturantsystem.controllers.Products;

import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.ProductRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.ProductResponse;
import com.saranaresturantsystem.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "Endpoints for managing restaurant products and inventory")
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products with pagination and filters")
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String, String> params) {
        Page<ProductResponse> productPage = productService.getAllProducts(params);
        PageDTO pageDTO = new PageDTO(productPage);

        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .message("Products loaded successfully")
                .payload(pageDTO)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a product by its ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
        ProductResponse productResponse = productService.findById(id);

        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .succeess(true)
                .message("Product found successfully")
                .payload(productResponse)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new product with an optional image")
    public ResponseEntity<ApiResponse<ProductResponse>> create(
            @Valid @ModelAttribute ProductRequest request, // Use @ModelAttribute instead of @RequestPart
            @RequestParam(value = "file", required = false) MultipartFile file) {

        ProductResponse productResponse = productService.createProduct(request, file);

        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .succeess(true)
                .status(HttpStatus.CREATED)
                .message("Product created successfully")
                .payload(productResponse)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update product details and image")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestPart("product") ProductRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        ProductResponse productResponse = productService.updateProduct(id, request, file);

        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Product updated successfully")
                .payload(productResponse)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}")
    @Operation(summary = "Soft delete a product by changing its visibility")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.deleteProduct(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Product deleted successfully")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }
}