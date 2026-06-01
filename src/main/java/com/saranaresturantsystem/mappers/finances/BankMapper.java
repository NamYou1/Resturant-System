package com.saranaresturantsystem.mappers.finances;

import com.saranaresturantsystem.dto.request.finance.BankRequest;
import com.saranaresturantsystem.dto.response.finance.BankResponse;
import com.saranaresturantsystem.entities.finance.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BankMapper {
    BankResponse toBankResponse (Bank bank);
    @Mapping(target = "id",ignore = true)
    Bank toBank(BankRequest request);
    @Mapping(target = "id",ignore = true)
    void updateEntityFromRequest(BankRequest request, @MappingTarget Bank entity);
}
