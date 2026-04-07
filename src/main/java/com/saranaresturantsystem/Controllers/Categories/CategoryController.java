package com.saranaresturantsystem.Controllers.Categories;

import com.saranaresturantsystem.DTO.PageDTO;
import com.saranaresturantsystem.DTO.Request.CategoryRequest;
import com.saranaresturantsystem.DTO.Response.ApiResponse;
import com.saranaresturantsystem.DTO.Response.CategoryResponse;
import com.saranaresturantsystem.Services.CategoryService;
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

import java.beans.PropertyEditorSupport;
import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@Tag(name = "Category", description = "Endpoints for managing categories")
public class CategoryController {
    private final CategoryService categoryService;

    @InitBinder
    public  void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(MultipartFile.class, new PropertyEditorSupport(){;
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                // No need to implement this method for MultipartFile
            }

            @Override
            public void setValue(Object value) {
                if (value instanceof MultipartFile) {
                    super.setValue(value);
                } else {
                    super.setValue(null);
                }
            }
        });
    }



    /**
     * Get all categories with pagination
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getListCategory(
            @RequestParam Map<String , String> params
            ) {
        Page<CategoryResponse> categoryPage = categoryService.getListCategory(params);
        PageDTO pageDTO = new PageDTO(categoryPage);

        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Categories retrieved successfully")
                .payload(pageDTO)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Get category by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Category retrieved successfully")
                .payload(categoryResponse)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Create new category
     */
    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @ModelAttribute CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .succeess(true)
                .status(HttpStatus.CREATED)
                .message("Category created successfully")
                .payload(response)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * Update existing category
     */
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Category updated successfully")
                .payload(response)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Delete category
     */
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        ApiResponse<Void>
                response = ApiResponse.<Void>builder()
                .succeess(true)
                .status(HttpStatus.OK)
                .message("Category deleted successfully")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
}


