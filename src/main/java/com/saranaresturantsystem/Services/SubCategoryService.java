package com.saranaresturantsystem.Services;

import com.saranaresturantsystem.DTO.Request.SubCategoryRequest;
import com.saranaresturantsystem.DTO.Response.SubCategoryResponse;
import com.saranaresturantsystem.Enities.SubCategory;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface SubCategoryService {
    Page<SubCategoryResponse> getAllSubCategory(Map<String , String> params);
    SubCategory getSubCategoryById(Long id);
    SubCategoryResponse createSubCategory(SubCategoryRequest request);
    void deleteSubCategory(Long id );
    SubCategoryResponse updateSubCategory(Long id , SubCategoryRequest request);
    SubCategoryResponse findById (Long Id);

}
