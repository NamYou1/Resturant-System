package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.FileHandler;
import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.OptionRequest;
import com.saranaresturantsystem.dto.response.OptionResponse;
import com.saranaresturantsystem.entities.Options;
import com.saranaresturantsystem.entities.Product;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.OptionsMapper;
import com.saranaresturantsystem.mappers.ProductMapper;
import com.saranaresturantsystem.repositories.OptionsRepository;
import com.saranaresturantsystem.repositories.ProductRepository;
import com.saranaresturantsystem.repositories.ProductStoreQtyRepository;
import com.saranaresturantsystem.services.OptionService;
import com.saranaresturantsystem.specification.products.ProductFilter;
import com.saranaresturantsystem.specification.products.ProductSpec;
import com.saranaresturantsystem.specification.settings.options.OptionFilter;
import com.saranaresturantsystem.specification.settings.options.OptionSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import javax.swing.text.html.Option;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OptionServiceImp implements OptionService {
    private final OptionsRepository optionsRepository;

    private final OptionsMapper optionsMapper;
    private final ObjectMapper objectMapper ;
    private final UniqueChecker uniqueChecker;
    private  final FileHandler fileHandler ;
    @Override
    public Page<OptionResponse> getAllOption(Map<String, String> params) {
        OptionFilter filter = objectMapper.convertValue(params, OptionFilter.class);

        Pageable pageable = PageUtil.fromParams(params);

        Specification<Options> spec = OptionSpec.filterBy(filter);
        return optionsRepository.findAll(spec, pageable)
                .map(optionsMapper::toOptionsResponse);
    }

    @Override
    public Options getOptionById(Long id) {
        return optionsRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Option",id));

    }
    @Override
    public OptionResponse createOption(OptionRequest request) {
        Options options=optionsMapper.toOptions(request);
        uniqueChecker.verify(optionsRepository, options, "name", options.getName());
        Options savedExpenses = optionsRepository.save(options);
        return optionsMapper.toOptionsResponse(savedExpenses);
    }

    @Override
    public OptionResponse updateOption(Long id, OptionRequest request) {
        Options options=getOptionById(id);
        optionsMapper.updateOptions(request,options);
        uniqueChecker.verify(optionsRepository, options, "name", options.getName());
        Options updatedOption = optionsRepository.save(options);
        return optionsMapper.toOptionsResponse(updatedOption);
    }

    @Override
    public OptionResponse getOptionResponseById(Long id) {
        return optionsMapper.toOptionsResponse(getOptionById(id));
    }

    @Override
    public void deleteOption(Long id) {
        Options options=getOptionById(id);
        optionsRepository.save(options);
    }
}
