package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.CategoryRequest;
import com.saranaresturantsystem.dto.response.CategoryResponse;
import com.saranaresturantsystem.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

        CategoryResponse toCategoryResponse(Category category);
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "imageUrl"  , ignore = true)
        @Mapping(target = "subcategories" , ignore = true)
        Category toCategory(CategoryRequest request);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "imageUrl"  , ignore = true)
        @Mapping(target = "subcategories" , ignore = true)
        void updateEntityFromRequest(CategoryRequest request, @MappingTarget Category entity);
}
