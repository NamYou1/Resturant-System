package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.product.SubCategoryRequest;
import com.saranaresturantsystem.dto.response.product.SubCategoryResponse;
import com.saranaresturantsystem.entities.product.SubCategory;
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
