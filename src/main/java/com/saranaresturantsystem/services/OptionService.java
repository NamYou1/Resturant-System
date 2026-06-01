package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.sales.OptionRequest;
import com.saranaresturantsystem.dto.response.sales.OptionResponse;
import com.saranaresturantsystem.entities.product.Options;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface OptionService {
    Page<OptionResponse>getAllOption(Map<String,String>params);

    Options getOptionById(Long id);
    OptionResponse createOption(OptionRequest request);
    OptionResponse updateOption(Long id,OptionRequest request);
    OptionResponse getOptionResponseById(Long id);
    void deleteOption(Long id);

}
