package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.CategoryRequest;
import com.saranaresturantsystem.dto.response.CategoryResponse;
import com.saranaresturantsystem.entities.Category;
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
