package com.saranaresturantsystem.Services.Implements;

import com.saranaresturantsystem.DTO.Request.CategoryRequest;
import com.saranaresturantsystem.DTO.Response.CategoryResponse;
import com.saranaresturantsystem.Enities.Category;
import com.saranaresturantsystem.Execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.Mappers.CategoryMapper;
import com.saranaresturantsystem.Repositories.CategoryRepository;
import com.saranaresturantsystem.Services.ImageService;
import com.saranaresturantsystem.Services.Interfaces.CategoryService;
import com.saranaresturantsystem.Specification.Category.CategoryFilter;
import com.saranaresturantsystem.Specification.Category.CategorySpec;
import com.saranaresturantsystem.Utils.GloblePagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class CategoryServiceImp implements CategoryService {
    private  final CategoryRepository categoryRepository ;
    private  final ObjectMapper objectMapper ;
    private  final  CategoryMapper categoryMapper ;
    private  final ImageService imageService;

// Service implementation Getlist Category with filter and pagination
    @Override
    public Page<CategoryResponse> getListCategory(Map<String, String> params) {
        CategoryFilter filter = objectMapper.convertValue(params, CategoryFilter.class);
        int pageLimit  = params.containsKey(GloblePagination.DEFAULT_PAGE_LIMIT) ? Integer.parseInt(params.get(GloblePagination.DEFAULT_PAGE_LIMIT )) : 0;
        int size = params.containsKey(GloblePagination.PAGE_NUMBER) ? Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER)) : 10;
        Pageable pageable = PageRequest.of(pageLimit, size);
        Specification<Category> spec = CategorySpec.filterBy(filter);
        return  categoryRepository.findAll(spec, pageable).map(categoryMapper::toCategoryResponse);
    }

    // GetCategoryById
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundExecption("Category" ,id));
    }


    // Create Category with image upload to cloud
    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
            Category category = categoryMapper.toCategory(categoryRequest);
             if (categoryRequest.getImagePath() != null && !categoryRequest.getImagePath().isEmpty()) {                // store image on cloud and we need to add 2 params imagefile , folder for like category
                category.setImageUrl(imageService.uploadImage(categoryRequest.getImagePath() , "category"));
            }else {
                 category.setImageUrl("");
            }
            Category savedCategory = categoryRepository.save(category);

            return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = getCategoryById(id);
        categoryMapper.updateEntityFromRequest(categoryRequest, category);
        if (categoryRequest.getImagePath() != null) {
            // store image on cloud and we need to add 2 params imagefile , folder for like category
            category.setImageUrl(imageService.uploadImage(categoryRequest.getImagePath() , "category"));
        }
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category findCategory = getCategoryById(id);
        findCategory.setStatus("ina");
        categoryRepository.save(findCategory);
    }
}

