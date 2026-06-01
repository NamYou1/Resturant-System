package com.saranaresturantsystem.mappers.inventory;
import com.saranaresturantsystem.dto.request.finance.ExpensesTypeRequest;
import com.saranaresturantsystem.dto.response.finance.ExpensesTypeResponse;
import com.saranaresturantsystem.entities.finance.ExpensesType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExpensesTypeMapper {
    ExpensesTypeResponse toResponse(ExpensesType expensesType);
    @Mapping(target = "id",ignore = true)
    ExpensesType toExpensesType(ExpensesTypeRequest expensesTypeRequest);
    @Mapping(target = "id",ignore = true)
    void updateExpensesType(ExpensesTypeRequest expensesTypeRequest, @MappingTarget ExpensesType entity);
}
