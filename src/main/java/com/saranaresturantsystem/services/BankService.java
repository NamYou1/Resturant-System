package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.finance.BankRequest;
import com.saranaresturantsystem.dto.response.finance.BankResponse;
import com.saranaresturantsystem.entities.finance.Bank;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface BankService {
    Page<BankResponse> getListBank(Map<String,String> params);
    Bank getBankById(long id);
    BankResponse createBank(BankRequest bankRequest);
    BankResponse updateBank(Long id,BankRequest bankRequest);
    BankResponse getBankResponseById(Long id);
    void deleteBank(Long id);
}
