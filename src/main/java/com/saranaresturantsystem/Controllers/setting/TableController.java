package com.saranaresturantsystem.controllers.setting;

import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.TableRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.TableResponse;
import com.saranaresturantsystem.services.TableService;
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
@RequestMapping("/api/v1/table")
@RequiredArgsConstructor
@Tag(name = "Table", description = "Endpoints for managing tables")
public class TableController {
    private final TableService tableService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getTables(
            @RequestParam Map<String , String> params
            ) {
        Page<TableResponse> tablePage = tableService.getAllTables(params);
        PageDTO pageDTO = new PageDTO(tablePage);
        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Tables retrieved successfully")
                .timestamp(Instant.now())
                .payload(pageDTO)
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
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

    @PostMapping("/{id}")
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
