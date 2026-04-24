package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.ProductRequest;
import com.saranaresturantsystem.dto.response.ProductResponse;
import com.saranaresturantsystem.entities.Product;
import com.saranaresturantsystem.services.CategoryService;
import com.saranaresturantsystem.services.SubCategoryService;
import com.saranaresturantsystem.services.UnitServices;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CategoryService.class, SubCategoryService.class, UnitServices.class})
public interface ProductMapper {

    // Map Entity to Response DTO
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "sectionId", source = "section.id")
    @Mapping(target = "sectionName", source = "section.section")
    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitName", source = "unit.name")
    ProductResponse toProductResponse(Product product);

    // Map Request DTO to Entity (Uses Services to find objects by ID)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "section", source = "sectionId")
    @Mapping(target = "unit", source = "unitId")
    @Mapping(target = "image", defaultValue = "no_image.png")
    Product toProduct(ProductRequest request);

    // Update existing Entity from Request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "section", source = "sectionId")
    @Mapping(target = "unit", source = "unitId")
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}