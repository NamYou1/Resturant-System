package com.saranaresturantsystem.Services.Implements;

import com.saranaresturantsystem.Common.UniqueChecker;
import com.saranaresturantsystem.DTO.Request.SubCategoryRequest;
import com.saranaresturantsystem.DTO.Response.SubCategoryResponse;
import com.saranaresturantsystem.Enities.SubCategory;
import com.saranaresturantsystem.Execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.Mappers.SubCategoryMapper;
import com.saranaresturantsystem.Repositories.SubCategoryRepository;
import com.saranaresturantsystem.Services.SubCategoryService;
import com.saranaresturantsystem.Specification.Category.SubCategoryFilter;
import com.saranaresturantsystem.Specification.Category.SubCategorySpec;
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
public class SubCategoryServiceImp implements SubCategoryService {
    private  final SubCategoryRepository subCategoryRepository;
    private final ObjectMapper objectMapper ;
    private  final SubCategoryMapper subCategoryMapper ;
    private  final UniqueChecker uniqueChecker;

    @Override
    public Page<SubCategoryResponse> getAllSubCategory(Map<String, String> params) {
        SubCategoryFilter filter = objectMapper.convertValue(params, SubCategoryFilter.class);
        int pageLimit = params.containsKey(GloblePagination.DEFAULT_PAGE_LIMIT)? Integer.parseInt(params.get(GloblePagination.DEFAULT_PAGE_LIMIT)) : GloblePagination.DEFAULT_PAGE_LIMIT;
        int size = params.containsKey(GloblePagination.DEFAULT_PAGE_NUMBER)? Integer.parseInt(params.get(GloblePagination.DEFAULT_PAGE_NUMBER)) : GloblePagination.DEFAULT_PAGE_NUMBER;
        Pageable pageable = PageRequest.of(size, pageLimit);
        Specification<SubCategory> spec = SubCategorySpec.filterBy(filter);
        return subCategoryRepository.findAll(spec  , pageable).map(subCategoryMapper::toSubCategoryResponse);
    }

    @Override
    public SubCategory getSubCategoryById(Long id) {
        return subCategoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundExecption("SubCategory" , id));
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
        exitId.setStatus("inc");
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
