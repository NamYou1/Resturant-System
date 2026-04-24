package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.SupplierRequest;
import com.saranaresturantsystem.dto.response.SupplierResponse;
import com.saranaresturantsystem.entities.Supplier;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.mappers.SupplerMapper;
import com.saranaresturantsystem.repositories.SupplierRepository;
import com.saranaresturantsystem.specification.peoples.suppliers.SupplerFilter;
import com.saranaresturantsystem.specification.peoples.suppliers.SupplerSpec;
import com.saranaresturantsystem.utils.GloblePagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupplierServiceImp implements com.saranaresturantsystem.services.SupplierService {
    private  final SupplierRepository supplierRepository;
    private  final SupplerMapper supplerMapper ;
    private  final ObjectMapper objectMapper ;
    private  final UniqueChecker uniqueChecker ;


    @Override
    public Page<SupplierResponse> getListSupplier(Map<String, String> params) {
        SupplerFilter filter = objectMapper.convertValue(params, SupplerFilter.class);
        int pageNumber = params.containsKey(GloblePagination.PAGE_NUMBER)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER))
                : GloblePagination.DEFAULT_PAGE_NUMBER;
        int pageSize = params.containsKey(GloblePagination.PAGE_LIMIT)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_LIMIT))
                : GloblePagination.DEFAULT_PAGE_LIMIT;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Specification<Supplier> spec = SupplerSpec.filterBy(filter);
        return  supplierRepository.findAll(spec, pageable).map(supplerMapper::toSupplierResponse);
    }

    @Override
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = supplerMapper.toSuppler(request);
        uniqueChecker.verify(supplierRepository , supplier , "name" , supplier.getName());
        uniqueChecker.verify(supplierRepository , supplier , "phone" , supplier.getPhone());
        uniqueChecker.verify(supplierRepository , supplier , "email" , supplier.getEmail());
        Supplier savedSupplier = supplierRepository.save(supplier);
        return  supplerMapper.toSupplierResponse(savedSupplier);
    }

    @Override
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = findSupplierById(id);
        supplerMapper.updateEnityFromRequest(request, supplier);
        uniqueChecker.verify(supplierRepository , supplier , "name" , supplier.getName());
        uniqueChecker.verify(supplierRepository , supplier , "phone" , supplier.getPhone());
        uniqueChecker.verify(supplierRepository , supplier , "email" , supplier.getEmail());
        Supplier updateSupplier = supplierRepository.save(supplier);
        return  supplerMapper.toSupplierResponse(updateSupplier);
    }

    @Override
    public Supplier findSupplierById(Long Id) {
        return supplierRepository.findById(Id).orElseThrow(()->new ResourceNotFoundExecption("Supplier " , Id));
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = findSupplierById(id);
        supplier.setStatus(GeneralStatus.INACTIVE);
        supplierRepository.save(supplier);
    }

    @Override
    public SupplierResponse getSupplierById(Long id) {
        return supplerMapper.toSupplierResponse(findSupplierById(id));
    }
}
