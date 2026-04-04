package com.saranaresturantsystem.Services.Implements;

import com.saranaresturantsystem.Common.UniqueChecker;
import com.saranaresturantsystem.DTO.Request.SupplierRequest;
import com.saranaresturantsystem.DTO.Response.SupplierResponse;
import com.saranaresturantsystem.Enities.Supplier;
import com.saranaresturantsystem.Execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.Mappers.SupplerMapper;
import com.saranaresturantsystem.Repositories.SupplierRepository;
import com.saranaresturantsystem.Specification.People.SupplerFilter;
import com.saranaresturantsystem.Specification.People.SupplerSpec;
import com.saranaresturantsystem.Utils.GloblePagination;
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
public class SupplierService implements com.saranaresturantsystem.Services.SupplierService {
    private  final SupplierRepository supplierRepository;
    private  final SupplerMapper supplerMapper ;
    private  final ObjectMapper objectMapper ;
    private  final UniqueChecker uniqueChecker ;


    @Override
    public Page<SupplierResponse> getListSupplier(Map<String, String> params) {
        SupplerFilter filter = objectMapper.convertValue(params, SupplerFilter.class);
        int pageLimit  = params.containsKey(GloblePagination.DEFAULT_PAGE_LIMIT) ? Integer.parseInt(params.get(GloblePagination.DEFAULT_PAGE_LIMIT )) : 0;
        int size = params.containsKey(GloblePagination.PAGE_NUMBER) ? Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER)) : 10;
        Pageable pageable = PageRequest.of(pageLimit, size);
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
        supplier.setStatus("inc");
        supplierRepository.save(supplier);
    }

    @Override
    public SupplierResponse getSupplierById(Long id) {
        return supplerMapper.toSupplierResponse(findSupplierById(id));
    }
}
