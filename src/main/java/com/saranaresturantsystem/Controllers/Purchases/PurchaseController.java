package com.saranaresturantsystem.controllers.Purchases;


import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.PurchaseRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.PurchaseResponse;
import com.saranaresturantsystem.services.PurchasesService;
package com.saranaresturantsystem.Controllers.Purchases;

import com.saranaresturantsystem.DTO.PageDTO;
import com.saranaresturantsystem.DTO.Request.PurchaseRequest;
import com.saranaresturantsystem.DTO.Response.ApiResponse;
import com.saranaresturantsystem.DTO.Response.PurchaseResponse;
import com.saranaresturantsystem.Services.PurchasesService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/purchases")
@Tag(name = "Purchase", description = "Endpoints for managing inventory purchases and stock entry")
public class PurchaseController {

    private final PurchasesService purchasesService;

    @PostMapping
    @Operation(summary = "Create purchase, update stock, and record transaction with Seller ID")
    public ResponseEntity<ApiResponse<PurchaseResponse>> create(@Valid @RequestBody PurchaseRequest request) {
        PurchaseResponse response = purchasesService.createPurchase(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<PurchaseResponse>builder()
                        .succeess(true)
                        .status(HttpStatus.CREATED)
                        .message("Purchase processed successfully by Seller ID: " + request.getSellerId())
                        .payload(response)
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @GetMapping
    @Operation(summary = "Get list of purchases with pagination")
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String, String> params) {
        Page<PurchaseResponse> purchasePage = purchasesService.getList(params);
        return ResponseEntity.ok(
                ApiResponse.<PageDTO>builder()
                        .succeess(true)
                        .status(HttpStatus.OK)
                        .message("Purchases retrieved successfully")
                        .payload(new PageDTO(purchasePage))
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase details by ID")
    public ResponseEntity<ApiResponse<PurchaseResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.<PurchaseResponse>builder()
                        .succeess(true)
                        .status(HttpStatus.OK)
                        .payload(purchasesService.findById(id))
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a purchase record")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        purchasesService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .succeess(true)
                        .status(HttpStatus.OK)
                        .message("Purchase record soft-deleted successfully")
                        .timestamp(Instant.now())
                        .build()
        );
    }
}