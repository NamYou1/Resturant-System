package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.sales.SellerRequest;
import com.saranaresturantsystem.dto.response.sales.SellerResponse;
import com.saranaresturantsystem.entities.sales.Seller;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SellerService {
    Page<SellerResponse> getList(Map<String, String> params);
    SellerResponse create(SellerRequest request);
    SellerResponse update(Long id, SellerRequest request);
    SellerResponse findById(Long id);
    Seller getById(Long id );
    List<SellerResponse> findAll();
    void delete(Long id);
}