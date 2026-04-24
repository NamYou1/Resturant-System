package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.CurrencyRequest;
import com.saranaresturantsystem.dto.response.CurrencyResponse;
import com.saranaresturantsystem.entities.Currency;
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
