package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.SubCategoryRequest;
import com.saranaresturantsystem.dto.response.SubCategoryResponse;
import com.saranaresturantsystem.entities.SubCategory;
import com.saranaresturantsystem.services.CategoryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {CategoryService.class} ,componentModel = "spring")
public interface SubCategoryMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    SubCategoryResponse toSubCategoryResponse(SubCategory subCategory);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    SubCategory toSubCategory(SubCategoryRequest request);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    void updateSubCategoryFromRequest(SubCategoryRequest request, @MappingTarget SubCategory subCategory);

}
