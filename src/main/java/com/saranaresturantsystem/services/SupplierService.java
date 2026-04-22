package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.SupplierRequest;
import com.saranaresturantsystem.dto.response.SupplierResponse;
import com.saranaresturantsystem.entities.Supplier;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface SupplierService {
    Page<SupplierResponse> getListSupplier(Map<String , String> params);
    SupplierResponse createSupplier(SupplierRequest request);
    SupplierResponse updateSupplier(Long id, SupplierRequest request);
    Supplier findSupplierById(Long Id );
    void deleteSupplier(Long id);
    SupplierResponse getSupplierById(Long id );

}
