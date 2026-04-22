package com.saranaresturantsystem.controllers.Categories;

import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.SubCategoryRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.SubCategoryResponse;
import com.saranaresturantsystem.services.SubCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "SubCategory", description = "Endpoints for managing subcategories")
@RequestMapping("/api/v1/subcategory")
public class SubCategoryController {
    private  final SubCategoryService subCategoryService ;

    /**
     * GetAll SubCategories
     * @param params
     * @return
     */

    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getList(@RequestParam Map<String , String> params){
        Page<SubCategoryResponse> subCategoryResponsePage = subCategoryService.getAllSubCategory(params);
        PageDTO pageDTO = new PageDTO(subCategoryResponsePage);
        ApiResponse<PageDTO> subCategoryPage = ApiResponse.<PageDTO>builder().succeess(true).message("subCategory load successfully..").payload(pageDTO).timestamp(Instant.now()).build();
        return  ResponseEntity.status(HttpStatus.OK).body(subCategoryPage);
    }

    /**
     *
     * get SubCategory By Id  @param id
     * @return
     */
    @GetMapping("/{id}")
    public  ResponseEntity<ApiResponse<SubCategoryResponse>> getById(@PathVariable Long id ){
        SubCategoryResponse categoryResponse =subCategoryService.findById(id);
        ApiResponse<SubCategoryResponse> response = ApiResponse.<SubCategoryResponse>builder().succeess(true)
                .message("findSupplierById subCategory By Id").payload(categoryResponse).timestamp(Instant.now()).build();
        return  ResponseEntity.ok(response);
    }

    /**
     *
     * Create SubCategory with category id it's  a has a relation with category
     * @param request
     * @return
     */
    @PostMapping
    public  ResponseEntity<ApiResponse<SubCategoryResponse>> create (@Valid @RequestBody SubCategoryRequest request){
        SubCategoryResponse categoryResponse = subCategoryService.createSubCategory(request);
        ApiResponse<SubCategoryResponse> response = ApiResponse.<SubCategoryResponse>builder()
                .succeess(true).status(HttpStatus.CREATED).message("create subCategory successfully").payload(categoryResponse).timestamp(Instant.now()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     *
     * update subCategory with category id it's  e relation with category
     * @param request
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public  ResponseEntity<ApiResponse<SubCategoryResponse>> update (@Valid @RequestBody SubCategoryRequest request , @PathVariable Long id){
        SubCategoryResponse categoryResponse = subCategoryService.updateSubCategory(id, request);
        ApiResponse<SubCategoryResponse> response = ApiResponse.<SubCategoryResponse>builder()
                .succeess(true).status(HttpStatus.OK).message("update subCategory successfully").payload(categoryResponse).timestamp(Instant.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * delete subcategory we just change status active to inactive data store in database not delete from database
     * @param id
     * @return
     */

    @PostMapping("/{id}")
    public  ResponseEntity<ApiResponse<Void>> delete (@PathVariable Long id ){
        subCategoryService.deleteSubCategory(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT)
                .succeess(true)
                .message("delete subCategory successfully")
                .timestamp(Instant.now())
                .build();
        return  ResponseEntity.ok(response);
    }



}
