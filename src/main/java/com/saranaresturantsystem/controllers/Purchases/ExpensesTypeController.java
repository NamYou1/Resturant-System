package com.saranaresturantsystem.controllers.Purchases;

import com.saranaresturantsystem.dto.PageDTO;

import com.saranaresturantsystem.dto.request.ExpensesTypeRequest;
import com.saranaresturantsystem.dto.response.ExpensesTypeResponse;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.services.ExpensesTypeService;
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
@RequestMapping("api/v1/expensestype")
@Tag(name="ExpensesType",description = "Endporints for managing expensestype")
public class ExpensesTypeController {
    private final ExpensesTypeService expensesTypeService;
    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getListExpensesType(
            @RequestParam Map<String, String> params
    ) {

        Page<ExpensesTypeResponse> expensesTypeResponsePage =
                expensesTypeService.getListExpensesType(params);

        PageDTO pageDTO = new PageDTO(expensesTypeResponsePage);

        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("ExpensesType list retrieved successfully")
                .payload(pageDTO)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpensesTypeResponse>>getExpensesTypeById(@PathVariable Long id){
        ExpensesTypeResponse expensesTypeResponse=expensesTypeService.getExpensesTypeResponseById(id);
        ApiResponse<ExpensesTypeResponse> response=ApiResponse.<ExpensesTypeResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("ExpensesType retrieved successfully")
                .payload(expensesTypeResponse)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<ExpensesTypeResponse>>createExpensesType(@Valid @RequestBody ExpensesTypeRequest request){
        ExpensesTypeResponse response=expensesTypeService.createExpensesType(request);
        ApiResponse<ExpensesTypeResponse>apiResponse=ApiResponse.<ExpensesTypeResponse>builder()
                .succeess(true)
                .status(HttpStatus.CREATED)
                .message("ExpensesType created successfully")
                .payload(response)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<ExpensesTypeResponse>>updateExpensesType(
            @PathVariable Long id,
            @Valid @RequestBody com.saranaresturantsystem.dto.request.ExpensesTypeRequest request){
        ExpensesTypeResponse response=expensesTypeService.updateExpensesType(id,request);
        ApiResponse<ExpensesTypeResponse>apiResponse=ApiResponse.<ExpensesTypeResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("ExpensesType update successfully!!")
                .payload(response)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>>deleteExpensesType(@PathVariable Long id){
        expensesTypeService.deleteExpensesType(id);
        ApiResponse<Void>
                response=ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("ExpensesType delete successfully")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
}

