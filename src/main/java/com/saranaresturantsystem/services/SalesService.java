package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.sales.SaleRequest;
import com.saranaresturantsystem.dto.response.sales.SaleResponse;
import com.saranaresturantsystem.entities.sales.Sale;
import com.saranaresturantsystem.enums.SaleStatus;

import java.util.Map;

import org.springframework.data.domain.Page;

public interface SalesService {
    SaleResponse create(SaleRequest request, String createdBy);

    SaleResponse getById(Long id);
    Page<SaleResponse> getAll(Map<String,String> params);
    SaleResponse update(Long id, SaleRequest request, String updatedBy);
    SaleResponse approve(Long id, String updatedBy);
    SaleResponse complete(Long id, String updatedBy);
    SaleResponse cancel(Long id, String updatedBy);
    SaleResponse returnSale(Long id, String updatedBy);
    void delete(Long id, String deletedBy);

    // Backward compatibility
    Sale findById(Long id);
    SaleResponse updateStatus(Long id, SaleStatus status, String updatedBy);
}