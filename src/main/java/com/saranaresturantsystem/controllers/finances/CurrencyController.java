package com.saranaresturantsystem.controllers.finances;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.finance.CurrencyRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.finance.CurrencyResponse;
import com.saranaresturantsystem.services.CurrencyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Currency", description = "Endpoints for managing currencies")
@RequestMapping("/api/v1/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    @PreAuthorize("hasAuthority('currency:read')")
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(currencyService.getAll(params), "Currency");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('currency:read')")
    public ResponseEntity<ApiResponse<CurrencyResponse>> getById(@Positive @PathVariable Long id) {
        return ResponseFactory.ok(currencyService.getCurrencyById(id), Message.getById("Currency", id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('currency:create')")
    public ResponseEntity<ApiResponse<CurrencyResponse>> create(@Valid @RequestBody CurrencyRequest request) {
        return ResponseFactory.created(currencyService.createCurrency(request), "Currency");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('currency:update')")
    public ResponseEntity<ApiResponse<CurrencyResponse>> update(@PathVariable Long id, @Valid @RequestBody CurrencyRequest request) {
        return ResponseFactory.ok(currencyService.updateCurrency(id, request), Message.updated("Currency", id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('currency:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        currencyService.deleteCurrency(id);
        return ResponseFactory.deleted("Currency", id);
    }
}
