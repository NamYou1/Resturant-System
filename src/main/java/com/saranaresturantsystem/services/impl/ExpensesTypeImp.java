package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.Request.ExpensesTypeRequest;
import com.saranaresturantsystem.dto.Response.ExpensesTypeResponse;
import com.saranaresturantsystem.entities.ExpensesType;
import com.saranaresturantsystem.entities.status.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.ExpensesTypeMapper;
import com.saranaresturantsystem.repositories.ExpensesTypeRepository;
import com.saranaresturantsystem.services.ExpensesTypeService;
import com.saranaresturantsystem.specification.purchases.ExpensesType.ExpensesTypeFilter;
import com.saranaresturantsystem.specification.purchases.ExpensesType.ExpensesTypeSpec;
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
public class ExpensesTypeImp implements ExpensesTypeService {
    private final ExpensesTypeRepository expensesTypeRepository;
    private final ObjectMapper objectMapper;
    private final ExpensesTypeMapper expensesTypeMapper;
    private final UniqueChecker uniqueChecker;

    @Override
    public Page<ExpensesTypeResponse> getListExpensesType(Map<String, String> params) {
        ExpensesTypeFilter expensesTypeFilter = objectMapper.convertValue(params,ExpensesTypeFilter.class);
        int pageLimit=params.containsKey(PageUtil.PAGE_NUMBER)
                ? Integer.parseInt(params.get(PageUtil.PAGE_NUMBER))
                :PageUtil.DEFAULT_PAGE_SIZE;
        int pageSize = params.containsKey(PageUtil.PAGE_LIMIT)
                ? Integer.parseInt(params.get(PageUtil.PAGE_LIMIT))
                : PageUtil.DEFAULT_PAGE;
        Pageable pageable= PageRequest.of(pageLimit,pageSize);
        Specification<ExpensesType> spec= ExpensesTypeSpec.filterBy(expensesTypeFilter);
        return expensesTypeRepository.findAll(pageable).map(expensesTypeMapper::toResponse);
    }

    @Override
    public ExpensesType getExpensesTypeById(long id) {
        return expensesTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ExpensesType", id));
    }

    @Override
    public ExpensesTypeResponse createExpensesType(ExpensesTypeRequest expensesTypeRequest) {
        ExpensesType expensesType=expensesTypeMapper.toExpensesType(expensesTypeRequest);
        uniqueChecker.verify(expensesTypeRepository,expensesType,"name",expensesType.getName());
        uniqueChecker.verify(expensesTypeRepository,expensesType,"description",expensesType.getDescription());

        ExpensesType updatedExpensesType=expensesTypeRepository.save(expensesType);
        return expensesTypeMapper.toResponse(updatedExpensesType);
    }

    @Override
    public ExpensesTypeResponse updateExpensesType(Long id, ExpensesTypeRequest expensesTypeRequest) {
        ExpensesType expensesType=getExpensesTypeById(id);
        expensesTypeMapper.updateExpensesType(expensesTypeRequest,expensesType);
        uniqueChecker.verify(expensesTypeRepository,expensesType,"name",expensesType.getName());
        uniqueChecker.verify(expensesTypeRepository,expensesType,"description",expensesType.getDescription());

        ExpensesType updatedExpensesType=expensesTypeRepository.save(expensesType);
        return expensesTypeMapper.toResponse(updatedExpensesType);
    }

    @Override
    public ExpensesTypeResponse getExpensesTypeResponseById(Long id) {
        return expensesTypeMapper.toResponse(getExpensesTypeById(id));
    }

    @Override
    public void deleteExpensesType(Long id) {
        ExpensesType expensesType=getExpensesTypeById(id);
        expensesType.setStatus(StatusType.ACTIVE);
        expensesTypeRepository.save(expensesType);
    }
}
