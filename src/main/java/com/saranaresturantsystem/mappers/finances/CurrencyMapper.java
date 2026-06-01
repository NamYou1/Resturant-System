package com.saranaresturantsystem.mappers.finances;

import com.saranaresturantsystem.dto.request.finance.CurrencyRequest;
import com.saranaresturantsystem.dto.response.finance.CurrencyResponse;
import com.saranaresturantsystem.entities.finance.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyResponse toResponse(Currency currency);
    @Mapping(target = "id", ignore = true)
    Currency toEntities (CurrencyRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(CurrencyRequest request , @MappingTarget Currency entity);
}
