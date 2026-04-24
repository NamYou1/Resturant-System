package com.saranaresturantsystem.controllers.setting;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.UnitRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.UnitResponse;
import com.saranaresturantsystem.services.UnitServices;
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
@Tag(name = "Unit", description = "Endpoints for managing units of measurement")
@RequestMapping("/api/v1/units")
public class UnitController {
    private final UnitServices unitServices;
    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String, String> params) {
        Page<UnitResponse> unitPage = unitServices.getAllUnits(params);
        PageDTO pageDTO = new PageDTO(unitPage);

        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .message("Units loaded successfully")
                .payload(pageDTO)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitResponse>> getById(@PathVariable Long id) {
        UnitResponse unitResponse = unitServices.findById(id);

        ApiResponse<UnitResponse> response = ApiResponse.<UnitResponse>builder()
                .succeess(true)
                .message("Unit found successfully")
                .payload(unitResponse)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<UnitResponse>> create(@Valid @RequestBody UnitRequest request) {
        UnitResponse unitResponse = unitServices.createUnit(request);

        ApiResponse<UnitResponse> response = ApiResponse.<UnitResponse>builder()
                .succeess(true)
                .status(HttpStatus.CREATED)
                .message("Unit created successfully")
                .payload(unitResponse)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitResponse>> update(@PathVariable Long id, @Valid @RequestBody UnitRequest request) {
        UnitResponse unitResponse = unitServices.updateUnit(id, request);

        ApiResponse<UnitResponse> response = ApiResponse.<UnitResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Unit updated successfully")
                .payload(unitResponse)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        unitServices.deleteUnit(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Unit deleted successfully")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }
}