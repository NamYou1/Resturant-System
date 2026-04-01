package com.saranaresturantsystem.Mappers;

import com.saranaresturantsystem.DTO.Request.CategoryRequest;
import com.saranaresturantsystem.DTO.Response.CategoryResponse;
import com.saranaresturantsystem.Enities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

//@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public interface CategoryMapper {

        CategoryResponse toCategoryResponse(Category category);
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "imageUrl"  , ignore = true)
        Category toCategory(CategoryRequest request);
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "imageUrl"  , ignore = true)
        void updateEntityFromRequest(CategoryRequest request, @MappingTarget Category entity);
}
