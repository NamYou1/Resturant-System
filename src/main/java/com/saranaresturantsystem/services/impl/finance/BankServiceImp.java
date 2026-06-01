package com.saranaresturantsystem.services.impl.finance;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.finance.BankRequest;
import com.saranaresturantsystem.dto.response.finance.BankResponse;
import com.saranaresturantsystem.entities.finance.Bank;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.finances.BankMapper;
import com.saranaresturantsystem.repositories.finances.BankRepository;
import com.saranaresturantsystem.services.BankService;
import com.saranaresturantsystem.specification.finances.banks.BankFilter;
import com.saranaresturantsystem.specification.finances.banks.BankSpec;
import com.saranaresturantsystem.utils.PageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        Pageable pageable = PageUtil.fromParams(params);
        Specification<Bank> spec = BankSpec.filterBy(bankFilter);
        return bankRepository.findAll(spec, pageable).map(bankMapper::toBankResponse);
    }

    @Override
    public Bank getBankById(long id) {
        Bank exitId = bankRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Bank",id));
        if (exitId.getStatus() == StatusType.INACTIVE || exitId.getStatus() == null) {
            throw new ResourceNotFoundException("Bank", id);
        }
        return exitId;
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
        Bank updateBank=bankRepository.save(bank);
        return bankMapper.toBankResponse(updateBank);
    }

    @Override
    public BankResponse getBankResponseById(Long id) {
        return bankMapper.toBankResponse(getBankById(id));
    }

    @Override
    public void deleteBank(Long id) {
        Bank bank=getBankById(id);
        bank.setStatus(StatusType.INACTIVE);
        bankRepository.save(bank);
    }
}
