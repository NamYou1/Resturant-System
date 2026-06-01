package com.saranaresturantsystem.services;
import com.saranaresturantsystem.dto.request.finance.ExpensesTypeRequest;
import com.saranaresturantsystem.dto.response.finance.ExpensesTypeResponse;

import com.saranaresturantsystem.entities.finance.ExpensesType;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ExpensesTypeService {
    Page<ExpensesTypeResponse> getListExpensesType(Map<String,String>params);
    ExpensesType getExpensesTypeById(long id);
    ExpensesTypeResponse createExpensesType(ExpensesTypeRequest expensesTypeRequest);
    ExpensesTypeResponse updateExpensesType(Long id, ExpensesTypeRequest expensesTypeRequest);
    ExpensesTypeResponse getExpensesTypeResponseById(Long id);
    void deleteExpensesType(Long id);
}
