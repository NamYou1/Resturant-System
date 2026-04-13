package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.dto.request.CategoryRequest;
import com.saranaresturantsystem.dto.response.CategoryResponse;
import com.saranaresturantsystem.entities.Category;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.mappers.CategoryMapper;
import com.saranaresturantsystem.repositories.CategoryRepository;
import com.saranaresturantsystem.common.FileHandler;
import com.saranaresturantsystem.services.CategoryService;
import com.saranaresturantsystem.specification.categories.category.CategoryFilter;
import com.saranaresturantsystem.specification.categories.category.CategorySpec;
import com.saranaresturantsystem.utils.GloblePagination;
import com.saranaresturantsystem.common.UniqueChecker;
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
    private  final FileHandler fileHandler ;
    private  final  UniqueChecker uniqueChecker;


    // Service implementation Getlist Category with filter and pagination
    @Override
    public Page<CategoryResponse> getListCategory(Map<String, String> params) {
            CategoryFilter filter = objectMapper.convertValue(params, CategoryFilter.class);
            int pageNumber = params.containsKey(GloblePagination.PAGE_NUMBER)
                    ? Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER))
                    : GloblePagination.DEFAULT_PAGE_NUMBER;
            int pageSize = params.containsKey(GloblePagination.PAGE_LIMIT)
                    ? Integer.parseInt(params.get(GloblePagination.PAGE_LIMIT))
                    : GloblePagination.DEFAULT_PAGE_LIMIT;
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Specification<Category> spec = CategorySpec.filterBy(filter);
            return  categoryRepository.findAll(spec, pageable).map(categoryMapper::toCategoryResponse);
    }


    // get by from table category we need to add private coz we don't wanna response enity to user
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundExecption("Category" ,id));
    }

    // return categoryResponse to users
    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryMapper.toCategoryResponse(findCategoryById(id));
    }


    // Create Category with image upload to cloud
    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
            Category category = categoryMapper.toCategory(categoryRequest);
             if (categoryRequest.getImagePath() != null && !categoryRequest.getImagePath().isEmpty()) {                // store image on cloud and we need to add 2 params imagefile , folder for like category
                category.setImageUrl(fileHandler.uploadImage(categoryRequest.getImagePath() , "category"));
            }else {
                 category.setImageUrl("");
            }
             // check duplicated
        uniqueChecker.verify(categoryRepository, category, "code", category.getCode());
        uniqueChecker.verify(categoryRepository, category, "name", category.getName());
            Category savedCategory = categoryRepository.save(category);

            return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = findCategoryById(id);
        categoryMapper.updateEntityFromRequest(categoryRequest, category);
        if (categoryRequest.getImagePath() != null) {
            // store image on cloud and we need to add 2 params imagefile , folder for like category
            category.setImageUrl(fileHandler.uploadImage(categoryRequest.getImagePath() , "category"));
        }
        uniqueChecker.verify(categoryRepository, category, "code",  category.getCode());
        uniqueChecker.verify(categoryRepository, category, "name", category.getName());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category findCategory = findCategoryById(id);
        findCategory.setStatus(GeneralStatus.INACTIVE);
        categoryRepository.save(findCategory);
    }
}

