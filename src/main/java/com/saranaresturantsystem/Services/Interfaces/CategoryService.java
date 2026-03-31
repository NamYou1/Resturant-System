package com.saranaresturantsystem.Services.Interfaces;

import com.saranaresturantsystem.DTO.Request.CategoryRequest;
import com.saranaresturantsystem.DTO.Response.CategoryResponse;
import com.saranaresturantsystem.Enities.Category;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CategoryService {
    Page<CategoryResponse> getListCategory(Map<String , String> params);
    
    Category getCategoryById(Long id);
    
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    
    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);
    
    void deleteCategory(Long id);
}
