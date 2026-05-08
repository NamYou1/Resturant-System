package com.saranaresturantsystem.controllers.adjustments;

import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.Request.AdjustmentRequest;
import com.saranaresturantsystem.dto.Response.AdjustmentResponse;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.services.AdjustmentService;
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
@RequestMapping("api/v1/adjustment")
@Tag(name = "Adjustment", description = "Endpoints for managing adjustment")
public class AdjustmentController {
    private final AdjustmentService adjustmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String, String> params) {
        Page<AdjustmentResponse> adjustmentPage = adjustmentService.getList(params);
        PageDTO pageDTO = new PageDTO(adjustmentPage);
        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Adjustment retrieved successfully")
                .timestamp(Instant.now())
                .payload(pageDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdjustmentResponse>> getAdjustmentById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<AdjustmentResponse>builder().succeess(true).status(HttpStatus.OK).message("Adjustment with id = "+ id+" retrieved successfully").payload(adjustmentService.findById(id)).timestamp(Instant.now()).build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdjustmentResponse>> createAdjustment(@Valid @RequestBody AdjustmentRequest request){
        AdjustmentResponse response = adjustmentService.createAdjustment(request);
        ApiResponse<AdjustmentResponse> apiResponse = ApiResponse.<AdjustmentResponse>builder().succeess(true).status(HttpStatus.CREATED).message("Adjustment create successfully").payload(response).timestamp(Instant.now()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAdjustment(@PathVariable Long id) {
        adjustmentService.deleteAdjustment(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Adjustment deleted successfully")
                .payload(null)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}
