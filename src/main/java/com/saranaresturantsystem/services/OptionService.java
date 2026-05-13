package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.OptionRequest;
import com.saranaresturantsystem.dto.response.OptionResponse;
import com.saranaresturantsystem.entities.Options;
import org.springframework.data.domain.Page;

import javax.swing.text.html.Option;
import java.util.Map;

public interface OptionService {
    Page<OptionResponse>getAllOption(Map<String,String>params);

    Options getOptionById(Long id);
    OptionResponse createOption(OptionRequest request);
    OptionResponse updateOption(Long id,OptionRequest request);
    OptionResponse getOptionResponseById(Long id);
    void deleteOption(Long id);

}
