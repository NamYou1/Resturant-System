package com.saranaresturantsystem.controllers.purchases;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.finance.ExpensesTypeRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.finance.ExpensesTypeResponse;
import com.saranaresturantsystem.services.ExpensesTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/expensestype")
@Tag(name = "ExpensesType", description = "Endpoints for managing expense types")
public class ExpensesTypeController {

    private final ExpensesTypeService expensesTypeService;

    @GetMapping
    @PreAuthorize("hasAuthority('expensesType:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(expensesTypeService.getListExpensesType(params), "ExpensesType");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('expensesType:read')")
    public ResponseEntity<ApiResponse<ExpensesTypeResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(expensesTypeService.getExpensesTypeResponseById(id), Message.getById("ExpensesType", id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('expensesType:create')")
    public ResponseEntity<ApiResponse<ExpensesTypeResponse>> create(@Valid @RequestBody ExpensesTypeRequest request) {
        return ResponseFactory.created(expensesTypeService.createExpensesType(request), "ExpensesType");
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('expensesType:update')")
    public ResponseEntity<ApiResponse<ExpensesTypeResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ExpensesTypeRequest request) {
        return ResponseFactory.ok(expensesTypeService.updateExpensesType(id, request), Message.updated("ExpensesType", id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('expensesType:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        expensesTypeService.deleteExpensesType(id);
        return ResponseFactory.deleted("ExpensesType", id);
    }
}
