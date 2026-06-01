package com.saranaresturantsystem.services.impl.product;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.product.SubCategoryRequest;
import com.saranaresturantsystem.dto.response.product.SubCategoryResponse;
import com.saranaresturantsystem.entities.product.SubCategory;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.product.SubCategoryMapper;
import com.saranaresturantsystem.repositories.product.SubCategoryRepository;
import com.saranaresturantsystem.services.SubCategoryService;
import com.saranaresturantsystem.specification.products.subCategory.SubCategoryFilter;
import com.saranaresturantsystem.specification.products.subCategory.SubCategorySpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class SubCategoryServiceImp implements SubCategoryService {
    private  final SubCategoryRepository subCategoryRepository;
    private final ObjectMapper objectMapper ;
    private  final SubCategoryMapper subCategoryMapper ;
    private  final UniqueChecker uniqueChecker;

    @Override
    public Page<SubCategoryResponse> getAllSubCategory(Map<String, String> params) {
        SubCategoryFilter filter = objectMapper.convertValue(params, SubCategoryFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<SubCategory> spec = SubCategorySpec.filterBy(filter);
        return subCategoryRepository.findAll(spec  , pageable).map(subCategoryMapper::toSubCategoryResponse);
    }

    @Override
    public SubCategory getSubCategoryById(Long id) {
        return subCategoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("SubCategory" , id));
    }

    @Override
    public SubCategoryResponse createSubCategory(SubCategoryRequest request) {
        SubCategory subCategory = subCategoryMapper.toSubCategory(request);
        uniqueChecker.verify(subCategoryRepository , subCategory , "section" , subCategory.getSection() );
        SubCategory saveSubCategory = subCategoryRepository.save(subCategory);
        return  subCategoryMapper.toSubCategoryResponse(saveSubCategory);
    }


    @Override
    public void deleteSubCategory(Long id) {
        SubCategory exitId = getSubCategoryById(id);
        exitId.setStatus(StatusType.INACTIVE);
       subCategoryRepository.save(exitId);
    }

    @Override
    public SubCategoryResponse updateSubCategory(Long id, SubCategoryRequest request) {
        SubCategory subCategory = getSubCategoryById(id);
        subCategoryMapper.updateSubCategoryFromRequest(request , subCategory);
        uniqueChecker.verify(subCategoryRepository , subCategory , "section" ,subCategory.getSection());
        SubCategory updateSubCategory = subCategoryRepository.save(subCategory);
        return  subCategoryMapper.toSubCategoryResponse(updateSubCategory);
    }

    @Override
    public SubCategoryResponse findById(Long Id) {
        return subCategoryMapper.toSubCategoryResponse(getSubCategoryById(Id));
    }


}
