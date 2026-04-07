package com.saranaresturantsystem.Controllers.Settings;

import com.saranaresturantsystem.DTO.Request.TableRequest;
import com.saranaresturantsystem.DTO.Response.ApiResponse;
import com.saranaresturantsystem.DTO.Response.TableResponse;
import com.saranaresturantsystem.Enities.TableEnities;
import com.saranaresturantsystem.Mappers.TableMapper;
import com.saranaresturantsystem.Services.TableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;


@RestController
@RequestMapping("/api/v1/table")
@RequiredArgsConstructor
@Tag(name = "Table", description = "Endpoints for managing tables")
public class TableController {
    private final TableService tableService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TableResponse>>> getTables() {
        return ResponseEntity.ok(ApiResponse.<List<TableResponse>>builder()
                .succeess(true)
                .message("Tables loaded successfully")
                .payload(tableService.getAllTables())
                .timestamp(Instant.now())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TableResponse>> getTableById(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<TableResponse>builder()
                .succeess(true)
                .message("Table retrieved successfully")
                .payload(tableService.getTableById(id))
                .timestamp(Instant.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TableResponse>> createTable(@Valid @RequestBody TableRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<TableResponse>builder()
                .succeess(true)
                .message("Table created successfully")
                .payload(tableService.createTable(request))
                .timestamp(Instant.now())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TableResponse>> updateTable(@PathVariable Long id, @Valid @RequestBody TableRequest request) {
        return ResponseEntity.ok(ApiResponse.<TableResponse>builder()
                .succeess(true)
                .message("Table updated successfully")
                .payload(tableService.updateTable(id, request))
                .timestamp(Instant.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .succeess(true)
                .message("Table deleted successfully")
                .payload(null)
                .timestamp(Instant.now())
                .build());
    }
}
