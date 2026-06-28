package com.saranaresturantsystem.controllers.sales;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.sales.OptionRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.sales.OptionResponse;
import com.saranaresturantsystem.services.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Option", description = "Endpoints for managing restaurant options")
@RequestMapping("/api/v1/options")
public class OptionController {

    private final OptionService optionService;

    @GetMapping
    @Operation(summary = "Get all options with pagination and filters")
    @PreAuthorize("hasAuthority('option:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(optionService.getAllOption(params), "Option");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find an option by its ID")
    @PreAuthorize("hasAuthority('option:read')")
    public ResponseEntity<ApiResponse<OptionResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(optionService.getOptionResponseById(id), Message.getById("Option", id));
    }

    @PostMapping
    @Operation(summary = "Create a new option")
    @PreAuthorize("hasAuthority('option:create')")
    public ResponseEntity<ApiResponse<OptionResponse>> create(@Valid @RequestBody OptionRequest request) {
        return ResponseFactory.created(optionService.createOption(request), "Option");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update option details")
    @PreAuthorize("hasAuthority('option:update')")
    public ResponseEntity<ApiResponse<OptionResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute OptionRequest request) {
        return ResponseFactory.ok(optionService.updateOption(id, request), Message.updated("Option", id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an option")
    @PreAuthorize("hasAuthority('option:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        optionService.deleteOption(id);
        return ResponseFactory.deleted("Option", id);
    }
}
