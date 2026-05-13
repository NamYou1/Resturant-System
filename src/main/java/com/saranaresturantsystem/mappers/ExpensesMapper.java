package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.ExpensesRequest;
import com.saranaresturantsystem.dto.response.ExpensesResponse;
import com.saranaresturantsystem.entities.Expenses;
import com.saranaresturantsystem.services.BankService;
import com.saranaresturantsystem.services.ExpensesTypeService;
import com.saranaresturantsystem.services.StoreService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                StoreService.class,
                BankService.class,
                ExpensesTypeService.class
        }
)
public interface ExpensesMapper {

    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")

    @Mapping(target = "bankId", source = "bank.id")
    @Mapping(target = "bankName", source = "bank.name")

    @Mapping(target = "expensesTypeId", source = "expensesType.id")
    @Mapping(target = "expensesTypeName", source = "expensesType.name")
    ExpensesResponse toExpensesResponse(Expenses expenses);


    @Mapping(target = "store", source = "storeId")
    @Mapping(target = "bank", source = "bankId")
    @Mapping(target = "expensesType", source = "expensesTypeId")
    Expenses toExpenses(ExpensesRequest request);

//    @Mapping(y)
    @Mapping(target = "store", source = "storeId")
    @Mapping(target = "bank", source = "bankId")
    @Mapping(target = "expensesType", source = "expensesTypeId")
    void updateExpensesFromRequest(ExpensesRequest request,
                      @MappingTarget Expenses expenses);
}