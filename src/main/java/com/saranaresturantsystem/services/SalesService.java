package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.SaleRequest;
import com.saranaresturantsystem.dto.response.SaleResponse;
import org.springframework.data.domain.Page;
import java.util.Map;

public interface SalesService {
    SaleResponse createSale(SaleRequest request);
    Page<SaleResponse> getList(Map<String, String> params);
    SaleResponse findById(Long id);
    void delete(Long id);
}