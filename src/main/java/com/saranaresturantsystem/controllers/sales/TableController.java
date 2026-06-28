package com.saranaresturantsystem.controllers.sales;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.sales.TableRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.sales.TableResponse;
import com.saranaresturantsystem.services.TableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/table")
@RequiredArgsConstructor
@Tag(name = "Table", description = "Endpoints for managing tables")
public class TableController {

    private final TableService tableService;

    @GetMapping
    @PreAuthorize("hasAuthority('table:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(tableService.getAllTables(params), "Table");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('table:read')")
    public ResponseEntity<ApiResponse<TableResponse>> getById(@Positive @PathVariable Long id) {
        return ResponseFactory.ok(tableService.getTableById(id), Message.getById("Table", id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('table:create')")
    public ResponseEntity<ApiResponse<TableResponse>> create(@Valid @RequestBody TableRequest request) {
        return ResponseFactory.created(tableService.createTable(request), "Table");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('table:update')")
    public ResponseEntity<ApiResponse<TableResponse>> update(@PathVariable Long id, @Valid @RequestBody TableRequest request) {
        return ResponseFactory.ok(tableService.updateTable(id, request), Message.updated("Table", id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('table:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseFactory.deleted("Table", id);
    }
}
