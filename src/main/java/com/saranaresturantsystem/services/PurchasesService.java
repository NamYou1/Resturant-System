package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.purchases.PurchaseRequest;
import com.saranaresturantsystem.dto.response.purchases.PurchaseResponse;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface PurchasesService {
    PurchaseResponse create(PurchaseRequest request);
    Page<PurchaseResponse> getAll(Map<String, String> params);
    PurchaseResponse getById(Long id);
    PurchaseResponse update(Long id, PurchaseRequest request, String updatedBy);
    PurchaseResponse approve(Long id, String updatedBy);
    PurchaseResponse complete(Long id, String updatedBy);
    PurchaseResponse cancel(Long id, String updatedBy);
    void delete(Long id, String deletedBy);
}