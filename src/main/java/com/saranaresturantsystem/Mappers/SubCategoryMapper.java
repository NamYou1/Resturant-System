package com.saranaresturantsystem.Mappers;

import com.saranaresturantsystem.DTO.Request.SubCategoryRequest;
import com.saranaresturantsystem.DTO.Response.SubCategoryResponse;
import com.saranaresturantsystem.Enities.SubCategory;
import com.saranaresturantsystem.Services.CategoryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {CategoryService.class} ,componentModel = "spring")
public interface SubCategoryMapper {
    @Mapping(target = "categoryId", source = "category.id")
    SubCategoryResponse toSubCategoryResponse(SubCategory subCategory);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    SubCategory toSubCategory(SubCategoryRequest request);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    void updateSubCategoryFromRequest(SubCategoryRequest request, @MappingTarget SubCategory subCategory);

}
