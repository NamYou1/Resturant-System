package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.FileHandler;
import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.ExpensesRequest;
import com.saranaresturantsystem.dto.response.ExpensesResponse;
import com.saranaresturantsystem.entities.Expenses;
import com.saranaresturantsystem.entities.status.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.ExpensesMapper;
import com.saranaresturantsystem.repositories.ExpensesRepository;
import com.saranaresturantsystem.services.ExpensesService;
import com.saranaresturantsystem.specification.purchases.Expenses.ExpensesFilter;
import com.saranaresturantsystem.specification.purchases.Expenses.ExpensesSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
@Service
@RequiredArgsConstructor
public class ExpensesServiceImp implements ExpensesService {
    private final ExpensesRepository expensesRepository;
    private final ExpensesMapper expensesMapper;
    private final ObjectMapper objectMapper ;

    private final UniqueChecker uniqueChecker;
    private final FileHandler fileHandler;

    @Override
    public Page<ExpensesResponse> getAllExpenses(Map<String, String> params) {
        ExpensesFilter filter=objectMapper.convertValue(params, ExpensesFilter.class);
        int pageNumber=params.containsKey(PageUtil.PAGE_NUMBER)
                ? Integer.parseInt(params.get(PageUtil.PAGE_NUMBER))
                : PageUtil.DEFAULT_PAGE_SIZE;
        int pageSize=params.containsKey(PageUtil.PAGE_LIMIT)
                ?Integer.parseInt(params.get(PageUtil.PAGE_LIMIT))
                : PageUtil.DEFAULT_PAGE_SIZE;
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Specification<Expenses> spec= ExpensesSpec.filterBy(filter);
        return expensesRepository.findAll(spec,pageable)
                .map(expensesMapper::toExpensesResponse);
    }

    @Override
    public Expenses getExpensesById(long id) {
        return expensesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Override
    public ExpensesResponse createExpenses(ExpensesRequest request) {
        Expenses expenses=expensesMapper.toExpenses(request);
        uniqueChecker.verify(expensesRepository, expenses, "name", expenses.getExpensesType());
        Expenses savedExpenses = expensesRepository.save(expenses);
        return expensesMapper.toExpensesResponse(savedExpenses);
    }

    @Override
    public ExpensesResponse updateExpenses(Long id, ExpensesRequest request) {
        Expenses expenses=getExpensesById(id);
        expensesMapper.updateExpensesFromRequest(request,expenses);
        uniqueChecker.verify(expensesRepository, expenses, "name", expenses.getExpensesType());
        Expenses updatedExpenses = expensesRepository.save(expenses);
        return expensesMapper.toExpensesResponse(updatedExpenses);
    }

    @Override
    public ExpensesResponse getExpensesResponseById(Long id) {
        return expensesMapper.toExpensesResponse(getExpensesById(id));
    }

    @Override
    public void deleteExpenses(Long id) {
        Expenses expenses=getExpensesById(id);
        expenses.setStatus(StatusType.INACTIVE);
        expensesRepository.save(expenses);

    }


}
