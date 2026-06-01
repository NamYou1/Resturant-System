package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.inventory.StoreRequest;
import com.saranaresturantsystem.dto.response.inventory.StoreResponse;
import com.saranaresturantsystem.entities.inventory.Store;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface StoreService {
    Page<StoreResponse> getAllStore(Map<String, String> params);
    StoreResponse create(StoreRequest request);
    StoreResponse update(Long id, StoreRequest request);
    StoreResponse getStoreById(Long id);
    void delete(Long id);
    Store findById(@Positive Long id);
}