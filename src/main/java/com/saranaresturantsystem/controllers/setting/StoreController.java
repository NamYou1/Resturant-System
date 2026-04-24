package com.saranaresturantsystem.controllers.setting;

import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.StoreRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.StoreResponse;
import com.saranaresturantsystem.services.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Tag(name = "Store", description = "Endpoints for managing stores")
public class StoreController {

    private final StoreService storeService;
    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getAllStore(
            @RequestParam Map<String , String> params
    ) {
        Page<StoreResponse> tablePage = storeService.getAllStore(params);
        PageDTO pageDTO = new PageDTO(tablePage);
        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Store retrieved successfully")
                .timestamp(Instant.now())
                .payload(pageDTO)
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreById(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<StoreResponse>builder()
                .succeess(true)
                .message("Store retrieved successfully")
                .payload(storeService.getStoreById(id))
                .timestamp(Instant.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(@Valid @RequestBody StoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<StoreResponse>builder()
                .succeess(true)
                .message("Store created successfully")
                .payload(storeService.create(request))
                .timestamp(Instant.now())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreResponse>> updateStore(@PathVariable Long id, @Valid @RequestBody StoreRequest request) {
        return ResponseEntity.ok(ApiResponse.<StoreResponse>builder()
                .succeess(true)
                .message("Store updated successfully")
                .payload(storeService.update(id, request))
                .timestamp(Instant.now())
                .build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStore(@PathVariable Long id) {
        storeService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .succeess(true)
                .message("Store deleted successfully")
                .payload(null)
                .timestamp(Instant.now())
                .build());
    }

}