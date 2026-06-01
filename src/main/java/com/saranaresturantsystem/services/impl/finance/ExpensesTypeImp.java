package com.saranaresturantsystem.services.impl.finance;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.finance.ExpensesTypeRequest;
import com.saranaresturantsystem.dto.response.finance.ExpensesTypeResponse;
import com.saranaresturantsystem.entities.finance.ExpensesType;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.inventory.ExpensesTypeMapper;
import com.saranaresturantsystem.repositories.inventory.ExpensesTypeRepository;
import com.saranaresturantsystem.services.ExpensesTypeService;
import com.saranaresturantsystem.specification.inventory.ExpensesType.ExpensesTypeFilter;
import com.saranaresturantsystem.specification.inventory.ExpensesType.ExpensesTypeSpec;
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
public class ExpensesTypeImp implements ExpensesTypeService {
    private final ExpensesTypeRepository expensesTypeRepository;
    private final ObjectMapper objectMapper;
    private final ExpensesTypeMapper expensesTypeMapper;
    private final UniqueChecker uniqueChecker;

    @Override
    public Page<ExpensesTypeResponse> getListExpensesType(Map<String, String> params) {
        ExpensesTypeFilter expensesTypeFilter = objectMapper.convertValue(params,ExpensesTypeFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<ExpensesType> spec= ExpensesTypeSpec.filterBy(expensesTypeFilter);
        return expensesTypeRepository.findAll(pageable).map(expensesTypeMapper::toResponse);
    }

    @Override
    public ExpensesType getExpensesTypeById(long id) {
        ExpensesType expensesType=  expensesTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ExpensesType", id));
        if (expensesType.getStatus() == StatusType.INACTIVE){
            throw  new ResourceNotFoundException("expensesType" , id);
        }
        return expensesType;
    }

    @Override
    public ExpensesTypeResponse createExpensesType(ExpensesTypeRequest expensesTypeRequest) {
        ExpensesType expensesType=expensesTypeMapper.toExpensesType(expensesTypeRequest);
        uniqueChecker.verify(expensesTypeRepository,expensesType,"name",expensesType.getName());
        ExpensesType updatedExpensesType=expensesTypeRepository.save(expensesType);
        return expensesTypeMapper.toResponse(updatedExpensesType);
    }

    @Override
    public ExpensesTypeResponse updateExpensesType(Long id, ExpensesTypeRequest expensesTypeRequest) {
        ExpensesType expensesType=getExpensesTypeById(id);
        expensesTypeMapper.updateExpensesType(expensesTypeRequest,expensesType);
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
        expensesType.setStatus(StatusType.INACTIVE);
        expensesTypeRepository.save(expensesType);
    }
}
