package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.BankRequest;
import com.saranaresturantsystem.dto.response.BankResponse;
import com.saranaresturantsystem.entities.Bank;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface BankService {
    Page<BankResponse> getListBank(Map<String,String> params);
    Bank getBankById(long id);
    BankResponse createBank(BankRequest bankRequest);
    BankResponse updateBank(Long id,BankRequest bankRequest);
    void deleteBank(Long id);
}
