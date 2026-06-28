package com.saranaresturantsystem.controllers.purchases;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.sales.OrderItemRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.sales.OrderItemResponse;
import com.saranaresturantsystem.services.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "OrderItem", description = "Endpoints for managing order items")
@RequestMapping("/api/v1/orderitem")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    @Operation(summary = "Get all order items with pagination and filters")
    @PreAuthorize("hasAuthority('orderItem:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(orderItemService.getAllOrderItem(params), "OrderItem");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find an order item by its ID")
    @PreAuthorize("hasAuthority('orderItem:read')")
    public ResponseEntity<ApiResponse<OrderItemResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(orderItemService.findById(id), Message.getById("OrderItem", id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new order item")
    @PreAuthorize("hasAuthority('orderItem:create')")
    public ResponseEntity<ApiResponse<OrderItemResponse>> create(@Valid @ModelAttribute OrderItemRequest request) {
        return ResponseFactory.created(orderItemService.createOrderItem(request), "OrderItem");
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update order item details")
    @PreAuthorize("hasAuthority('orderItem:update')")
    public ResponseEntity<ApiResponse<OrderItemResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute OrderItemRequest request) {
        return ResponseFactory.ok(orderItemService.updateOrderItem(id, request), Message.updated("OrderItem", id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete an order item")
    @PreAuthorize("hasAuthority('orderItem:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseFactory.deleted("OrderItem", id);
    }
}
