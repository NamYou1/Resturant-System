package com.saranaresturantsystem.services.impl.purchases;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.purchases.SupplierRequest;
import com.saranaresturantsystem.dto.response.purchases.SupplierResponse;
import com.saranaresturantsystem.entities.purchases.Supplier;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.purchases.SupplerMapper;
import com.saranaresturantsystem.repositories.purchases.SupplierRepository;
import com.saranaresturantsystem.specification.users.suppliers.SupplerFilter;
import com.saranaresturantsystem.specification.users.suppliers.SupplerSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        Pageable pageable = PageUtil.fromParams(params);

        Specification<Supplier> spec = SupplerSpec.filterBy(filter);
        return  supplierRepository.findAll(spec, pageable).map(supplerMapper::toSupplierResponse);
    }

    @Override
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = supplerMapper.toSuppler(request);
        return getSupplierResponse(supplier);
    }

    // reusable code
    private SupplierResponse getSupplierResponse(Supplier supplier) {
        uniqueChecker.verify(supplierRepository , supplier , "name" , supplier.getName());
        uniqueChecker.verify(supplierRepository , supplier , "phone" , supplier.getPhone());
        uniqueChecker.verify(supplierRepository , supplier , "email" , supplier.getEmail());
        Supplier savedSupplier = supplierRepository.save(supplier);
        return  supplerMapper.toSupplierResponse(savedSupplier);
    }

    @Override
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = findSupplierById(id);
        supplerMapper.updateEntityFromRequest(request, supplier);
        return getSupplierResponse(supplier);
    }

    @Override
    public Supplier findSupplierById(Long Id) {
        return supplierRepository.findById(Id).orElseThrow(()->new ResourceNotFoundException("Supplier " , Id));
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = findSupplierById(id);
        supplier.setStatus(StatusType.INACTIVE);
        supplierRepository.save(supplier);
    }

    @Override
    public SupplierResponse getSupplierById(Long id) {
        return supplerMapper.toSupplierResponse(findSupplierById(id));
    }
}
