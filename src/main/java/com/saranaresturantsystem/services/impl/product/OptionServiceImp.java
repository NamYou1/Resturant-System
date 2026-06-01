package com.saranaresturantsystem.services.impl.product;

import com.saranaresturantsystem.common.CloudinaryService;
import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.sales.OptionRequest;
import com.saranaresturantsystem.dto.response.sales.OptionResponse;
import com.saranaresturantsystem.entities.product.Options;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.sales.OptionsMapper;
import com.saranaresturantsystem.repositories.sales.OptionsRepository;
import com.saranaresturantsystem.services.OptionService;
import com.saranaresturantsystem.specification.sales.options.OptionFilter;
import com.saranaresturantsystem.specification.sales.options.OptionSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OptionServiceImp implements OptionService {
    private final OptionsRepository optionsRepository;

    private final OptionsMapper optionsMapper;
    private final ObjectMapper objectMapper ;
    private final UniqueChecker uniqueChecker;
    private  final CloudinaryService cloudinaryService;
    @Override
    public Page<OptionResponse> getAllOption(Map<String, String> params) {
        OptionFilter filter = objectMapper.convertValue(params, OptionFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<Options> spec = OptionSpec.filterBy(filter);
        return optionsRepository.findAll(spec, pageable).map(optionsMapper::toOptionsResponse);
    }

    @Override
    public Options getOptionById(Long id) {
        Options options = optionsRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Option",id));
        if (options.getIsActive() == StatusType.INACTIVE){
          throw  new ResourceNotFoundException("Option is not active"  , id);
        }
        return  options;

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
        options.setIsActive(StatusType.INACTIVE);
        optionsRepository.save(options);
    }
}
