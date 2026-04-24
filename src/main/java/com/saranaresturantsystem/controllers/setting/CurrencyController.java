package com.saranaresturantsystem.controllers.setting;

import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.CurrencyRequest;
import com.saranaresturantsystem.dto.request.TableRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.CurrencyResponse;
import com.saranaresturantsystem.dto.response.TableResponse;
import com.saranaresturantsystem.services.CurrencyService;
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
@Tag(name = "Currency", description = "Endpoints for managing currencies")
@RequestMapping("/api/v1/currency")
public class CurrencyController {
    private  final CurrencyService currencyService ;

    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getAllCurrency(
            @RequestParam Map<String , String> params
    ) {
        Page<CurrencyResponse> tablePage = currencyService.getAll(params);
        PageDTO pageDTO = new PageDTO(tablePage);
        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Currency retrieved successfully")
                .timestamp(Instant.now())
                .payload(pageDTO)
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CurrencyResponse>> getTableById(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<CurrencyResponse>builder()
                .succeess(true)
                .message("Currency retrieved successfully")
                .payload(currencyService.getCurrencyById(id))
                .timestamp(Instant.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CurrencyResponse>> createTable(@Valid @RequestBody CurrencyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<CurrencyResponse>builder()
                .succeess(true)
                .message("Currency created successfully")
                .payload(currencyService.createCurrency(request))
                .timestamp(Instant.now())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CurrencyResponse>> updateTable(@PathVariable Long id, @Valid @RequestBody CurrencyRequest request) {
        return ResponseEntity.ok(ApiResponse.<CurrencyResponse>builder()
                .succeess(true)
                .message("Currency updated successfully")
                .payload(currencyService.updateCurrency(id, request))
                .timestamp(Instant.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTable(@PathVariable Long id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .succeess(true)
                .message("Currency deleted successfully")
                .payload(null)
                .timestamp(Instant.now())
                .build());
    }
}
