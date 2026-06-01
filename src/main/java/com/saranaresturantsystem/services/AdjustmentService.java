package com.saranaresturantsystem.services;



import com.saranaresturantsystem.dto.request.inventory.AdjustmentRequest;
import com.saranaresturantsystem.dto.response.inventory.AdjustmentResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface AdjustmentService {
    Page<AdjustmentResponse> getList(Map<String, String> params);

    AdjustmentResponse findById(@Positive Long id);

    AdjustmentResponse createAdjustment(@Valid AdjustmentRequest request);

    void deleteAdjustment(Long id);
}
