package com.saranaresturantsystem.controllers.sales;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.sales.SellerRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.sales.SellerResponse;
import com.saranaresturantsystem.services.SellerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
@Tag(name = "Seller", description = "Endpoints for managing sellers")
public class SellerController {

    private final SellerService sellerService;

    /**
     * Get all sellers with pagination
     */
    @GetMapping
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(sellerService.getList(params), "Seller");
    }

    /**
     * Get seller by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<ApiResponse<SellerResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(sellerService.findById(id), Message.getById("Seller", id));
    }

    /**
     * Create new seller
     */
    @PostMapping
    @PreAuthorize("hasAuthority('seller:create')")
    public ResponseEntity<ApiResponse<SellerResponse>> create(@Valid @ModelAttribute SellerRequest request) {
        return ResponseFactory.created(sellerService.create(request), "Seller");
    }

    /**
     * Update existing seller
     */
    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('seller:update')")
    public ResponseEntity<ApiResponse<SellerResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute SellerRequest request) {
        return ResponseFactory.ok(sellerService.update(id, request), Message.updated("Seller", id));
    }

    /**
     * Delete seller
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('seller:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        sellerService.delete(id);
        return ResponseFactory.deleted("Seller", id);
    }
}
