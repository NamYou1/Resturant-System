package com.saranaresturantsystem.controllers.purchases;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.purchases.PurchaseRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.purchases.PurchaseResponse;
import com.saranaresturantsystem.services.PurchasesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchases")
@Tag(name = "Purchase", description = "Endpoints for managing inventory purchases and stock entry")
public class PurchaseController {

    private final PurchasesService purchasesService;

    @PostMapping
    @Operation(summary = "Create a new purchase order (status: ORDERED, no stock movement)")
    @PreAuthorize("hasAuthority('purchase:create')")
    public ResponseEntity<ApiResponse<PurchaseResponse>> create(@Valid @RequestBody PurchaseRequest request) {
        return ResponseFactory.created(purchasesService.create(request), "Purchase");
    }

    @GetMapping
    @Operation(summary = "Get list of purchases with pagination and filtering")
    @PreAuthorize("hasAuthority('purchase:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(purchasesService.getAll(params), "Purchase");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase details by ID")
    @PreAuthorize("hasAuthority('purchase:read')")
    public ResponseEntity<ApiResponse<PurchaseResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(purchasesService.getById(id), Message.getById("Purchase", id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an ORDERED purchase (note, date)")
    @PreAuthorize("hasAuthority('purchase:update')")
    public ResponseEntity<ApiResponse<PurchaseResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseRequest request,
            @RequestHeader(value = "X-Updated-By", defaultValue = "system") String updatedBy) {
        return ResponseFactory.ok(purchasesService.update(id, request, updatedBy), Message.updated("Purchase", id));
    }

    @PatchMapping("/{id}/approve")
    @Operation(summary = "Approve an ORDERED purchase (ORDERED → APPROVED)")
    @PreAuthorize("hasAuthority('purchase:approve')")
    public ResponseEntity<ApiResponse<PurchaseResponse>> approve(
            @PathVariable Long id,
            @RequestHeader(value = "X-Updated-By", defaultValue = "system") String updatedBy) {
        return ResponseFactory.ok(purchasesService.approve(id, updatedBy), "Purchase " + id + " approved successfully");
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete an APPROVED purchase (APPROVED → COMPLETED, stock updated)")
    @PreAuthorize("hasAuthority('purchase:completed')")
    public ResponseEntity<ApiResponse<PurchaseResponse>> complete(
            @PathVariable Long id,
            @RequestHeader(value = "X-Updated-By", defaultValue = "system") String updatedBy) {
        return ResponseFactory.ok(purchasesService.complete(id, updatedBy), "Purchase " + id + " completed successfully");
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel/return a purchase (reverses stock if completed)")
    @PreAuthorize("hasAuthority('purchase:cancel')")
    public ResponseEntity<ApiResponse<PurchaseResponse>> cancel(
            @PathVariable Long id,
            @RequestHeader(value = "X-Updated-By", defaultValue = "system") String updatedBy) {
        return ResponseFactory.ok(purchasesService.cancel(id, updatedBy), "Purchase " + id + " cancelled successfully");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a purchase (cannot delete completed purchases)")
    @PreAuthorize("hasAuthority('purchase:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @RequestHeader(value = "X-Updated-By", defaultValue = "system") String deletedBy) {
        purchasesService.delete(id, deletedBy);
        return ResponseFactory.deleted("Purchase", id);
    }
}