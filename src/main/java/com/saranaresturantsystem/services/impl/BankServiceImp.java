package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.BankRequest;
import com.saranaresturantsystem.dto.response.BankResponse;
import com.saranaresturantsystem.entities.Bank;
import com.saranaresturantsystem.entities.status.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.BankMapper;
import com.saranaresturantsystem.repositories.BankRepository;
import com.saranaresturantsystem.services.BankService;
import com.saranaresturantsystem.specification.settings.banks.BankFilter;
import com.saranaresturantsystem.specification.settings.banks.BankSpec;
import com.saranaresturantsystem.utils.PageUtil;
import jakarta.validation.Valid;
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

public class BankServiceImp implements BankService {
    private final BankRepository bankRepository;
    private final ObjectMapper objectMapper;
    private final BankMapper bankMapper;
    private final UniqueChecker uniqueChecker;

    @Override
    public Page<BankResponse> getListBank(Map<String, String> params) {
        BankFilter bankFilter = objectMapper.convertValue(params, BankFilter.class);
        int pageLimit = params.containsKey(PageUtil.PAGE_NUMBER)
                ? Integer.parseInt(params.get(PageUtil.PAGE_NUMBER))
                : PageUtil.DEFAULT_PAGE_SIZE;
        int pageSize = params.containsKey(PageUtil.PAGE_LIMIT)
                ? Integer.parseInt(params.get(PageUtil.PAGE_LIMIT))
                : PageUtil.DEFAULT_PAGE;
        Pageable pageable= PageRequest.of(pageLimit,pageSize);
        Specification<Bank> spec = BankSpec.filterBy(bankFilter);
        return bankRepository.findAll(pageable).map(bankMapper::toBankResponse);
    }
    //Getbankbyid
    @Override
    public Bank getBankById(long id) {
        return bankRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Bank",id));
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
    public BankResponse getBankResponseById(Long id) {
//        Bank bank=getBankById(id);
        return bankMapper.toBankResponse(getBankById(id));
    }

    @Override
    public void deleteBank(Long id) {
        Bank bank=getBankById(id);
        bank.setStatus(StatusType.INACTIVE);
        bankRepository.save(bank);
    }
}
