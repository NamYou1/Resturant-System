package com.saranaresturantsystem.Services.Implements;

import com.saranaresturantsystem.Common.FileHandler;
import com.saranaresturantsystem.Common.UniqueChecker;
import com.saranaresturantsystem.DTO.Request.BankRequest;
import com.saranaresturantsystem.DTO.Response.BankResponse;
import com.saranaresturantsystem.Enities.Bank;
import com.saranaresturantsystem.Execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.Mappers.BankMapper;
import com.saranaresturantsystem.Repositories.BankRepository;
import com.saranaresturantsystem.Services.Interfaces.BankService;
import com.saranaresturantsystem.Utils.GloblePagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@RequiredArgsConstructor
@Service

public class BankSeviceImp implements BankService {
    private final BankRepository bankRepository;
    private final ObjectMapper objectMapper;
    private final BankMapper bankMapper;
    private final FileHandler fileHandler;

    private final UniqueChecker uniqueChecker;

    @Override
    public Page<BankResponse> getListBank(Map<String, String> params) {
        int pageLimit=params.containsKey(GloblePagination.DEFAULT_PAGE_LIMIT)?Integer.parseInt(params.get(GloblePagination.DEFAULT_PAGE_LIMIT)):0;
        int size=params.containsKey(GloblePagination.PAGE_NUMBER)?Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER)):10;
        Pageable pageable= PageRequest.of(pageLimit,size);
        return bankRepository.findAll(pageable).map(bankMapper::toBankResponse);
    }
    //Getbankbyid
    @Override
    public Bank getBankById(long id) {
        return bankRepository.findById(id).orElseThrow(()->new ResourceNotFoundExecption("Ban",id));
    }

    @Override
    public BankResponse createBank(@Valid BankRequest bankRequest) {
        Bank bank =bankMapper.toBank(bankRequest);
        uniqueChecker.verify(bankRepository,bank,"name",bank.getName());
        uniqueChecker.verify(bankRepository,bank,"number",bank.getNumber());
        Bank savedBank=bankRepository.save(bank);
        return bankMapper.toBankResponse(savedBank);
    }
    @Override
    public BankResponse updateBank(Long id, BankRequest bankRequest) {
        Bank bank=getBankById(id);
        bankMapper.updateEntityFromRequest(bankRequest,bank);
        uniqueChecker.verify(bankRepository,bank,"name",bank.getName());
        uniqueChecker.verify(bankRepository,bank,"number",bank.getNumber());
        Bank updateBank=bankRepository.save(bank);
        return bankMapper.toBankResponse(updateBank);
    }
    @Override
    public void deleteBank(Long id) {
        Bank findBank=getBankById(id);
        findBank.setStatus("ina");
        bankRepository.save(findBank);
    }
}
