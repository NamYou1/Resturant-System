package com.saranaresturantsystem.controllers.Purchases;

import com.cloudinary.Api;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.OrderItemRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.OrderItemResponse;
import com.saranaresturantsystem.services.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name="OrderItem",description = "Endpoint for managing restaurant and inventory")
@RequestMapping("/api/v1/orderitem")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping
    @Operation(summary = "Get all orderitem with pagination and filter")
    public ResponseEntity<ApiResponse<PageDTO>>getList(@RequestParam Map<String,String>params){
        Page<OrderItemResponse>orderItemResponsePage=orderItemService.getAllOrderItem(params);
        PageDTO pageDTO=new PageDTO(orderItemResponsePage);
        ApiResponse<PageDTO>response=ApiResponse.<PageDTO>builder()
                .succeess(true)
                .message("Order load successfully")
                .payload(pageDTO)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Find a orderItem by it ID")
    public ResponseEntity<ApiResponse<OrderItemResponse>>getById(@PathVariable Long id){
        OrderItemResponse orderItemResponse=orderItemService.findById(id);
        ApiResponse<OrderItemResponse>response=ApiResponse.<OrderItemResponse>builder()
                .succeess(true)
                .message("OrderItem found successfully")
                .payload(orderItemResponse)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new orderitem")
    public ResponseEntity<ApiResponse<OrderItemResponse>>createOrderItem(
            @Valid @ModelAttribute OrderItemRequest request
            ){
        OrderItemResponse orderItemResponse=orderItemService.createOrderItem(request);
        ApiResponse<OrderItemResponse>response=ApiResponse.<OrderItemResponse>builder()
                .succeess(true)
                .status(HttpStatus.CREATED)
                .message("OrderItem create successfully")
                .payload(orderItemResponse)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update orderItem details ")
    public ResponseEntity<ApiResponse<OrderItemResponse>>updateOrderItem(
            @PathVariable Long id,
            @Valid @ModelAttribute OrderItemRequest request
    ){
        OrderItemResponse orderItemResponse=orderItemService.updateOrderItem(id, request);
        ApiResponse<OrderItemResponse>response=ApiResponse.<OrderItemResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Order updated successfully")
                .payload(orderItemResponse)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);

    }
    @PostMapping("/{id}")
    @Operation(summary = "Soft delete a order by changing its visibility")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Order deleted successfully")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
