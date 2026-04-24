package com.saranaresturantsystem.Services;

import com.saranaresturantsystem.DTO.Request.SellerRequest;
import com.saranaresturantsystem.DTO.Response.SellerResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SellerService {
    Page<SellerResponse> getList(Map<String, String> params);
    SellerResponse create(SellerRequest request);
    SellerResponse update(Long id, SellerRequest request);
    SellerResponse findById(Long id);
    List<SellerResponse> findAll();
    void delete(Long id);
}