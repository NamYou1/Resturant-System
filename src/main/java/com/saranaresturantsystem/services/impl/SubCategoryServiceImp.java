package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.SubCategoryRequest;
import com.saranaresturantsystem.dto.response.SubCategoryResponse;
import com.saranaresturantsystem.entities.SubCategory;
import com.saranaresturantsystem.entities.status.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.SubCategoryMapper;
import com.saranaresturantsystem.repositories.SubCategoryRepository;
import com.saranaresturantsystem.services.SubCategoryService;
import com.saranaresturantsystem.specification.categories.subCategory.SubCategoryFilter;
import com.saranaresturantsystem.specification.categories.subCategory.SubCategorySpec;
import com.saranaresturantsystem.utils.PageUtil;
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
public class SubCategoryServiceImp implements SubCategoryService {
    private  final SubCategoryRepository subCategoryRepository;
    private final ObjectMapper objectMapper ;
    private  final SubCategoryMapper subCategoryMapper ;
    private  final UniqueChecker uniqueChecker;

    @Override
    public Page<SubCategoryResponse> getAllSubCategory(Map<String, String> params) {
        SubCategoryFilter filter = objectMapper.convertValue(params, SubCategoryFilter.class);
        int pageNumber = params.containsKey(PageUtil.PAGE_NUMBER)
                ? Integer.parseInt(params.get(PageUtil.PAGE_NUMBER))
                : PageUtil.DEFAULT_PAGE_SIZE;
        int pageSize = params.containsKey(PageUtil.PAGE_LIMIT)
                ? Integer.parseInt(params.get(PageUtil.PAGE_LIMIT))
                : PageUtil.DEFAULT_PAGE;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
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
