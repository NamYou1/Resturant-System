package com.saranaresturantsystem.controllers.people;

import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.SellerRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.SellerResponse;
import com.saranaresturantsystem.services.SellerService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/sellers")
@Tag(name = "Seller", description = "Endpoints for managing sellers")
public class SellerController {
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getAllSeller(
            @RequestParam Map<String , String> params
    ) {
        Page<SellerResponse> sellerPage = sellerService.getList(params);
        PageDTO pageDTO = new PageDTO(sellerPage);
        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Seller retrieved successfully")
                .timestamp(Instant.now())
                .payload(pageDTO)
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SellerResponse>> getSellerById(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<SellerResponse>builder()
                .succeess(true)
                .message("Table retrieved successfully")
                .payload(sellerService.findById(id))
                .timestamp(Instant.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SellerResponse>> createTable(@Valid @RequestBody SellerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<SellerResponse>builder()
                .succeess(true)
                .message("Table created successfully")
                .payload(sellerService.create(request))
                .timestamp(Instant.now())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SellerResponse>> updateSeller(@PathVariable Long id, @Valid @RequestBody SellerRequest request) {
        return ResponseEntity.ok(ApiResponse.<SellerResponse>builder()
                .succeess(true)
                .message("Table updated successfully")
                .payload(sellerService.update(id, request))
                .timestamp(Instant.now())
                .build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSeller(@PathVariable Long id) {
        sellerService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .succeess(true)
                .message("Table deleted successfully")
                .payload(null)
                .timestamp(Instant.now())
                .build());
    }
}
