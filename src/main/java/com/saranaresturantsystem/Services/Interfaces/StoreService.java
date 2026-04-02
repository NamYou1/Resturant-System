package com.saranaresturantsystem.Services.Interfaces;

import com.saranaresturantsystem.DTO.Request.StoreRequest;
import com.saranaresturantsystem.DTO.Response.StoreResponse;
import java.util.List;

public interface StoreService {
    StoreResponse create(StoreRequest request);
    StoreResponse update(Long id, StoreRequest request);
    StoreResponse findById(Long id);
    List<StoreResponse> findAll();
    void delete(Long id);
}