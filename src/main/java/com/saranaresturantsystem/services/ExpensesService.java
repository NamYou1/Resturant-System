package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.ExpensesRequest;
import com.saranaresturantsystem.dto.response.ExpensesResponse;
import com.saranaresturantsystem.entities.Expenses;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ExpensesService {
   Page<ExpensesResponse>getAllExpenses(Map<String,String>params);
   Expenses getExpensesById(long id);
   ExpensesResponse createExpenses(ExpensesRequest expensesRequest);
   ExpensesResponse updateExpenses(Long id,ExpensesRequest expensesRequest);
   ExpensesResponse getExpensesResponseById(Long id);
   void deleteExpenses(Long id);
}
