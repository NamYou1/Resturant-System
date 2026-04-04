package com.saranaresturantsystem.Services;

import com.saranaresturantsystem.DTO.Request.CategoryRequest;
import com.saranaresturantsystem.DTO.Response.CategoryResponse;
import com.saranaresturantsystem.Enities.Category;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CategoryService {
    Page<CategoryResponse> getListCategory(Map<String , String> params);
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);
    Category findCategoryById (Long Id );
    void deleteCategory(Long id);
    CategoryResponse getCategoryById(Long id );
}
