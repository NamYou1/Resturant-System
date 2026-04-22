package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.StoreRequest;
import com.saranaresturantsystem.dto.response.StoreResponse;
import com.saranaresturantsystem.entities.Store;
import jakarta.validation.constraints.Positive;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface StoreService {
    Page<StoreResponse> getAllStore(Map<String, String> params);
    StoreResponse create(StoreRequest request);
    StoreResponse update(Long id, StoreRequest request);
    StoreResponse getStoreById(Long id);
    void delete(Long id);
    Store findById(@Positive Long id);
}