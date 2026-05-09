package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.PurchaseRequest;
import com.saranaresturantsystem.dto.response.PurchaseResponse;
import com.saranaresturantsystem.entities.Purchase;
import org.springframework.data.domain.Page;
import java.util.Map;
public interface PurchasesService {
    PurchaseResponse createPurchase(PurchaseRequest request);
    Page<PurchaseResponse> getList(Map<String, String> params);
    PurchaseResponse findById(Long id);
    void delete(Long id);
    Purchase getById(Long id);
}