package com.saranaresturantsystem.mappers;

import com.saranaresturantsystem.dto.request.BankRequest;
import com.saranaresturantsystem.dto.response.BankResponse;
import com.saranaresturantsystem.entities.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BankMapper {
    BankResponse toBankResponse (Bank bank);
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "status" , ignore = true)
    Bank toBank(BankRequest request);
    @Mapping(target = "id",ignore = true)
//    @Mapping(target = "status" , ignore = true)
    void updateEntityFromRequest(BankRequest request, @MappingTarget Bank entity);
}
