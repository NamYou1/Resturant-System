package com.saranaresturantsystem.Mappers;

import com.saranaresturantsystem.DTO.Request.BankRequest;
import com.saranaresturantsystem.DTO.Response.BankResponse;
import com.saranaresturantsystem.Enities.Bank;
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
