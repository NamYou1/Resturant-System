package com.saranaresturantsystem.services.impl.inventory;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.inventory.StoreRequest;
import com.saranaresturantsystem.dto.response.inventory.StoreResponse;
import com.saranaresturantsystem.entities.inventory.Store;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.inventory.StoreMapper;
import com.saranaresturantsystem.repositories.inventory.StoreRepository;
import com.saranaresturantsystem.services.StoreService;
import com.saranaresturantsystem.specification.inventory.stores.StoreFilter;
import com.saranaresturantsystem.specification.inventory.stores.StoreSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreServiceImp implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private  final ObjectMapper objectMapper ;
    private  final UniqueChecker uniqueChecker ;
    @Override
    public Page<StoreResponse> getAllStore(Map<String, String> params) {
        StoreFilter filter = objectMapper.convertValue(params, StoreFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<Store> spec = StoreSpec.filterBy(filter);
        return storeRepository.findAll(spec, pageable).map(storeMapper::toResponse);
    }

    @Override
    @Transactional
    public StoreResponse create(StoreRequest request) {
        Store store = storeMapper.toEntity(request);
        validateUniqueness(store);
//        uniqueChecker.verify(storeRepository , store , "name" , store.getName());
//        uniqueChecker.verify(storeRepository , store , "code" , store.getCode());
//        uniqueChecker.verify(storeRepository , store , "phone" , store.getPhone());
//        uniqueChecker.verify(storeRepository , store , "email" , store.getEmail());
        return storeMapper.toResponse(storeRepository.save(store));
    }

    @Override
    @Transactional
    public StoreResponse update(Long id, StoreRequest request) {
        Store store = findById(id);
        validateUniqueness(store);
//        uniqueChecker.verify(storeRepository , store , "name" , store.getName());
//        uniqueChecker.verify(storeRepository , store , "code" , store.getCode());
//        uniqueChecker.verify(storeRepository , store , "phone" , store.getPhone());
//        uniqueChecker.verify(storeRepository , store , "email" , store.getEmail());
        storeMapper.updateEntityFromRequest(request, store);
        return storeMapper.toResponse(storeRepository.save(store));
    }
    private  void validateUniqueness(Store store) {
        uniqueChecker.verify(storeRepository , store , "name" , store.getName());
        uniqueChecker.verify(storeRepository , store , "code" , store.getCode());
        uniqueChecker.verify(storeRepository , store , "phone" , store.getPhone());
        uniqueChecker.verify(storeRepository , store , "email" , store.getEmail());
    }

    @Override
    public StoreResponse getStoreById(Long id) {
        return storeMapper.toResponse(findById(id));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        Store store = findById(id);
        store.setStatus(StatusType.INACTIVE);
        storeRepository.save(store);
    }

    @Override
    public Store findById(Long id) {
        return  storeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Store" , id));
    }


}