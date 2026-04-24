package com.saranaresturantsystem.controllers.sales;

import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.SaleRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.SaleResponse;
import com.saranaresturantsystem.services.SalesService;
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
@RequestMapping("/api/v1/sales")
@Tag(name = "Sale", description = "Endpoints for managing sales, stock deduction, and customer transactions")
public class SaleController {
    private final SalesService salesService;
    @PostMapping
    @Operation(summary = "Create sale, deduct stock, and record transaction")
    public ResponseEntity<ApiResponse<SaleResponse>> create(@Valid @RequestBody SaleRequest request) {
        SaleResponse response = salesService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<SaleResponse>builder()
                        .succeess(true)
                        .status(HttpStatus.CREATED)
                        .message("Sale processed successfully. Stock updated.")
                        .payload(response)
                        .timestamp(Instant.now())
                        .build()
        );
    }
    @GetMapping
    @Operation(summary = "Get list of sales with pagination and filters")
    public ResponseEntity<ApiResponse<PageDTO>> getList(
            @RequestParam Map<String, String> params) {
        Page<SaleResponse> salePage = salesService.getList(params);
        return ResponseEntity.ok(
                ApiResponse.<PageDTO>builder()
                        .succeess(true)
                        .status(HttpStatus.OK)
                        .message("Sales retrieved successfully")
                        .payload(new PageDTO(salePage))
                        .timestamp(Instant.now())
                        .build()
        );
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get sale details by ID")
    public ResponseEntity<ApiResponse<SaleResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.<SaleResponse>builder()
                        .succeess(true)
                        .status(HttpStatus.OK)
                        .payload(salesService.findById(id))
                        .timestamp(Instant.now())
                        .build()
        );
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a sale record")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        salesService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .succeess(true)
                        .status(HttpStatus.OK)
                        .message("Sale record soft-deleted successfully")
                        .timestamp(Instant.now())
                        .build()
        );
    }
}