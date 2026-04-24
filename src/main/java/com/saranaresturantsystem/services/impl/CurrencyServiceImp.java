package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.CurrencyRequest;
import com.saranaresturantsystem.dto.response.CurrencyResponse;
import com.saranaresturantsystem.entities.Currency;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.mappers.CurrencyMapper;
import com.saranaresturantsystem.repositories.CurrencyRepository;
import com.saranaresturantsystem.services.CurrencyService;
import com.saranaresturantsystem.specification.settings.currency.CurrencyFilter;
import com.saranaresturantsystem.specification.settings.currency.CurrencySpec;
import com.saranaresturantsystem.utils.GloblePagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
@RequiredArgsConstructor
@Service
public class CurrencyServiceImp implements CurrencyService {
    private  final CurrencyRepository currencyRepository ;
    private  final UniqueChecker uniqueChecker ;
    private  final CurrencyMapper currencyMapper ;
    private  final ObjectMapper objectMapper ;
    @Override
    public Page<CurrencyResponse> getAll(Map<String, String> params) {
        CurrencyFilter filter = objectMapper.convertValue(params, CurrencyFilter.class);
        int pageNumber = params.containsKey(GloblePagination.PAGE_NUMBER)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER))
                : GloblePagination.DEFAULT_PAGE_NUMBER;
        int pageSize = params.containsKey(GloblePagination.PAGE_LIMIT)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_LIMIT))
                : GloblePagination.DEFAULT_PAGE_LIMIT;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Specification<Currency> spec = CurrencySpec.filterBy(filter);
        return  currencyRepository.findAll(spec, pageable).map(currencyMapper::toResponse);
    }

    @Override
    public CurrencyResponse createCurrency(CurrencyRequest request) {
        Currency currency = currencyMapper.toEntities(request);
        uniqueChecker.verify(currencyRepository , currency , "name" , currency.getName());
        uniqueChecker.verify(currencyRepository , currency , "code" , currency.getCode());
        Currency savedCurrency = currencyRepository.save(currency);
        return  currencyMapper.toResponse(savedCurrency);
    }

    @Override
    public CurrencyResponse getCurrencyById(Long id) {
        return currencyMapper.toResponse(getById(id));
    }

    @Override
    public CurrencyResponse updateCurrency(Long id, CurrencyRequest request) {
        Currency currency = getById(id);
        currencyMapper.updateEntityFromRequest(request, currency);
        uniqueChecker.verify(currencyRepository , currency , "name" , currency.getName());
        uniqueChecker.verify(currencyRepository , currency , "code" , currency.getCode());
        Currency updateCurrency = currencyRepository.save(currency);
        return  currencyMapper.toResponse(updateCurrency);
    }

    @Override
    public void deleteCurrency(Long id) {
        Currency currency = getById(id);
        currencyRepository.delete(currency);

    }

    @Override
    public Currency getById(Long id) {
        return currencyRepository.findById(id).orElseThrow(()-> new ResourceNotFoundExecption("Currency" ,id));
    }
}
