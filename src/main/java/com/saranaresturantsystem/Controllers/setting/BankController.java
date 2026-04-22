package com.saranaresturantsystem.controllers.setting;

import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.BankRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.BankResponse;
import com.saranaresturantsystem.entities.Bank;
import com.saranaresturantsystem.services.BankService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/bank")
@Tag(name="Bank",description = "Endpoints for managing banks")
public class BankController {
    private final BankService bankService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getListBank(
            @RequestParam Map<String , String> params
    ) {
        Page<BankResponse> bankPage = bankService.getListBank(params);
        PageDTO pageDTO = new PageDTO(bankPage);

        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Banks retrieved successfully")
                .payload(pageDTO)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Bank>> getBankById(@PathVariable Long id) {
        Bank banResponse = bankService.getBankById(id);
        ApiResponse<Bank> response = ApiResponse.<Bank>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Bank retrieved successfully")
                .payload(banResponse)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<BankResponse>> createBank(@Valid @RequestBody BankRequest request) {
        BankResponse response = bankService.createBank(request);
        ApiResponse<BankResponse> apiResponse = ApiResponse.<BankResponse>builder()
                .succeess(true)
                .status(HttpStatus.CREATED)
                .message("Banks created successfully")
                .payload(response)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<BankResponse>> updateBank(
            @PathVariable Long id,
            @Valid @RequestBody BankRequest request) {
        BankResponse response = bankService.updateBank(id, request);
        ApiResponse<BankResponse> apiResponse = ApiResponse.<BankResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Bank updated successfully")
                .payload(response)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>>deleteBank(@PathVariable Long id){
        bankService.deleteBank(id);
        ApiResponse<Void>
                response=ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Banks delete successful")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);

    }
}

