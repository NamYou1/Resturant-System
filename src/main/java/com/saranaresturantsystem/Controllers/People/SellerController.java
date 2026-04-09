package com.saranaresturantsystem.Controllers.People;

import com.saranaresturantsystem.DTO.PageDTO;
import com.saranaresturantsystem.DTO.Request.SellerRequest;
import com.saranaresturantsystem.DTO.Response.ApiResponse;
import com.saranaresturantsystem.DTO.Response.SellerResponse;
import com.saranaresturantsystem.Services.SellerService;
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
@RequestMapping("/api/v1/sellers")
@Tag(name = "Seller", description = "Endpoints for managing sellers/vendors")
public class SellerController {

    private final SellerService sellerService;

    @GetMapping
    @Operation(summary = "Get list of sellers with pagination and filters")
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String, String> params) {

        // 1. ទាញយកទិន្នន័យជា Page ពី Service
        Page<SellerResponse> sellerPage = sellerService.getList(params);

        // 2. ខ្ចប់វាចូលទៅក្នុង PageDTO ដើម្បីបំបែក data និង pagination
        PageDTO payload = new PageDTO(sellerPage);

        // 3. រៀបចំ Response
        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Sellers retrieved successfully")
                .payload(payload) // បញ្ចូល payload ដែលមានទិន្នន័យ និង pagination
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find seller by ID")
    public ResponseEntity<ApiResponse<SellerResponse>> getById(@PathVariable Long id) {
        SellerResponse sellerResponse = sellerService.findById(id);

        ApiResponse<SellerResponse> response = ApiResponse.<SellerResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Seller retrieved successfully")
                .payload(sellerResponse)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new seller")
    public ResponseEntity<ApiResponse<SellerResponse>> create(@Valid @RequestBody SellerRequest request) {
        SellerResponse response = sellerService.create(request);

        ApiResponse<SellerResponse> apiResponse = ApiResponse.<SellerResponse>builder()
                .succeess(true)
                .status(HttpStatus.CREATED)
                .message("Seller created successfully")
                .payload(response)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update seller details")
    public ResponseEntity<ApiResponse<SellerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody SellerRequest request) {

        SellerResponse response = sellerService.update(id, request);

        ApiResponse<SellerResponse> apiResponse = ApiResponse.<SellerResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Seller updated successfully")
                .payload(response)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * កែសម្រួល៖ ប្តូរមកប្រើ DeleteMapping ឱ្យត្រូវតាមស្តង់ដារ REST
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete seller")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        sellerService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Seller deleted successfully")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }
}