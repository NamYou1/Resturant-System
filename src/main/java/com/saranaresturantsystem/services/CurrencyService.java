package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.finance.CurrencyRequest;
import com.saranaresturantsystem.dto.response.finance.CurrencyResponse;
import com.saranaresturantsystem.entities.finance.Currency;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CurrencyService {
        Page<CurrencyResponse> getAll(Map<String , String> params);
        CurrencyResponse createCurrency(CurrencyRequest request);
        CurrencyResponse getCurrencyById(Long id);
        CurrencyResponse updateCurrency(Long id , CurrencyRequest request);
        void deleteCurrency(Long id);
        Currency getById(Long id );
}
