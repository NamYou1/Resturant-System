package com.saranaresturantsystem.controllers.setting;
import com.saranaresturantsystem.dto.request.OptionRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.response.OptionResponse;
import com.saranaresturantsystem.services.OptionService;
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
@Tag(name="Option",description = "Endpoints for managing restaurant expenses and inventory")
@RequestMapping("/api/v1/options")
public class OptionController {
    private final OptionService optionService;
    private final JsonMapper.Builder builder;

    @GetMapping
    @Operation(summary = "Get All option with pagination all filter")
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String,String> params){
        Page<OptionResponse> optionResponsePage=optionService.getAllOption(params);
        PageDTO pageDTO=new PageDTO(optionResponsePage);

        ApiResponse<PageDTO>response=ApiResponse.<PageDTO>builder()
                .succeess(true)
                .message("Option get successfully")
                .payload(pageDTO)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a option by its ID")
    public ResponseEntity<ApiResponse<OptionResponse>>getOptionById(@PathVariable Long id){
        OptionResponse optionResponse=optionService.getOptionResponseById(id);
        ApiResponse<OptionResponse> response= ApiResponse.<OptionResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("ExpensesType retrieved successfully")
                .payload(optionResponse)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new expenses with an expenses")
    public ResponseEntity<ApiResponse<OptionResponse>> createExpenses(
            @Valid @RequestBody OptionRequest request
    ){
        OptionResponse optionResponse=optionService.createOption(request);
        ApiResponse<OptionResponse>response=ApiResponse.<OptionResponse>builder()
                .succeess(true)
                .status(HttpStatus.CREATED)
                .message("Option created successfully")
                .payload(optionResponse)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update option detail")
    public ResponseEntity<ApiResponse<OptionResponse>> updateExpenses(
            @PathVariable Long id,
            @Valid @ModelAttribute OptionRequest request
    ){
        OptionResponse optionResponse=optionService.updateOption(id, request);
        ApiResponse<OptionResponse>response=ApiResponse.<OptionResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Option updated successfully")
                .payload(optionResponse)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}")
    @Operation(summary = "Delete a options by changing")
    public ResponseEntity<ApiResponse<Void>> deleteExpenses(@PathVariable Long id){
        optionService.deleteOption(id);
        ApiResponse<Void>response=ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Option deleted successfully")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }

}
