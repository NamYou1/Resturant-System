package com.saranaresturantsystem.Services;

import com.saranaresturantsystem.DTO.Request.PurchaseRequest;
import com.saranaresturantsystem.DTO.Response.PurchaseResponse;
import org.springframework.data.domain.Page;
import java.util.Map;
public interface PurchasesService {
    PurchaseResponse createPurchase(PurchaseRequest request);
    Page<PurchaseResponse> getList(Map<String, String> params);
    PurchaseResponse findById(Long id);
    void delete(Long id);
}