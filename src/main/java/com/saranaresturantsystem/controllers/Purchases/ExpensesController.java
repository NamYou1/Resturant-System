package com.saranaresturantsystem.controllers.Purchases;

import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.ExpensesRequest;
import com.saranaresturantsystem.dto.response.ExpensesResponse;
import com.saranaresturantsystem.services.ExpensesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.json.JsonMapper;
import java.beans.PropertyEditorSupport;
import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name="Expenses",description = "Endpoints for managing restaurant expenses and inventory")
@RequestMapping("/api/v1/expenses")
public class ExpensesController {
    private final ExpensesService expensesService;
    private final JsonMapper.Builder builder;

//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.registerCustomEditor(MultipartFile.class,new PropertyEditorSupport(){
//            @Override
//            public void setAsText(String text) throws IllegalArgumentException {
//
//            }
//            @Override
//            public void setValue(Object value) {
//                if(value instanceof MultipartFile){
//                    super.setValue(value);
//                }else {
//                    super.setValue(value);
//                }
//            }
//        });
//    }
    @GetMapping
    @Operation(summary = "Get All expenses with pagination all filter")
    public ResponseEntity<ApiResponse<PageDTO>>getList(@RequestParam Map<String,String>params){
        Page<ExpensesResponse>expensesResponsePage=expensesService.getAllExpenses(params);
        PageDTO pageDTO=new PageDTO(expensesResponsePage);

        ApiResponse<PageDTO>response=ApiResponse.<PageDTO>builder()
                .succeess(true)
                .message("Expenses get successfully")
                .payload(pageDTO)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a expenses by its ID")
    public ResponseEntity<ApiResponse<ExpensesResponse>>getExpensesById(@PathVariable Long id){
        ExpensesResponse expensesResponse=expensesService.getExpensesResponseById(id);
        ApiResponse<ExpensesResponse> response= ApiResponse.<ExpensesResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("ExpensesType retrieved successfully")
                .payload(expensesResponse)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new expenses with an expenses")
    public ResponseEntity<ApiResponse<ExpensesResponse>> createExpenses(
            @Valid @RequestBody ExpensesRequest request
    ){
        ExpensesResponse expensesResponse=expensesService.createExpenses(request);
        ApiResponse<ExpensesResponse>response=ApiResponse.<ExpensesResponse>builder()
                    .succeess(true)
                    .status(HttpStatus.CREATED)
                    .message("Product created successfully")
                    .payload(expensesResponse)
                    .timestamp(Instant.now())
                    .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpensesResponse>> updateExpenses(
            @PathVariable Long id,
            @Valid @ModelAttribute ExpensesRequest request
    ){
        ExpensesResponse expensesResponse=expensesService.updateExpenses(id, request);
        ApiResponse<ExpensesResponse>response=ApiResponse.<ExpensesResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Expenses updated successfully")
                .payload(expensesResponse)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpenses(@PathVariable Long id){
        expensesService.deleteExpenses(id);
        ApiResponse<Void>response=ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Expenses deleted successfully")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);

    }


}
