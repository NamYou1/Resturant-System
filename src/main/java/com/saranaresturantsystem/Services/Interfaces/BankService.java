package com.saranaresturantsystem.Services.Interfaces;

import com.saranaresturantsystem.DTO.Request.BankRequest;
import com.saranaresturantsystem.DTO.Response.BankResponse;
import com.saranaresturantsystem.Enities.Bank;
import com.saranaresturantsystem.Repositories.BankRepository;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface BankService {
    Page<BankResponse> getListBank(Map<String,String> params);
    Bank getBankById(long id);
    BankResponse createBank(BankRequest bankRequest);
    BankResponse updateBank(Long id,BankRequest bankRequest);
    void deleteBank(Long id);
}
