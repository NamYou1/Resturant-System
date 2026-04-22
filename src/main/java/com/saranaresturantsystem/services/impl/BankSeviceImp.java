package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.FileHandler;
import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.BankRequest;
import com.saranaresturantsystem.dto.response.BankResponse;
import com.saranaresturantsystem.entities.Bank;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.mappers.BankMapper;
import com.saranaresturantsystem.repositories.BankRepository;
import com.saranaresturantsystem.services.BankService;
import com.saranaresturantsystem.utils.GloblePagination;
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
        findBank.setStatus(GeneralStatus.INACTIVE);
        bankRepository.save(findBank);
    }
}
