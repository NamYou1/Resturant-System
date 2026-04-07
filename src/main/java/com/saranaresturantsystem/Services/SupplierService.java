package com.saranaresturantsystem.Services;

import com.saranaresturantsystem.DTO.Request.SupplierRequest;
import com.saranaresturantsystem.DTO.Response.SupplierResponse;
import com.saranaresturantsystem.Enities.Supplier;
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
