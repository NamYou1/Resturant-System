package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.StoreRequest;
import com.saranaresturantsystem.dto.response.StoreResponse;
import com.saranaresturantsystem.entities.Store;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.mappers.StoreMapper;
import com.saranaresturantsystem.repositories.StoreRepository;
import com.saranaresturantsystem.services.StoreService;
import com.saranaresturantsystem.specification.settings.stores.StoreFilter;
import com.saranaresturantsystem.specification.settings.stores.StoreSpec;
import com.saranaresturantsystem.utils.GloblePagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
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
        int pageNumber = params.containsKey(GloblePagination.PAGE_NUMBER)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER))
                : GloblePagination.DEFAULT_PAGE_NUMBER;
        int pageSize = params.containsKey(GloblePagination.PAGE_LIMIT)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_LIMIT))
                : GloblePagination.DEFAULT_PAGE_LIMIT;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Specification<Store> spec = StoreSpec.filterBy(filter);
        return storeRepository.findAll(spec, pageable).map(storeMapper::toResponse);
    }

    @Override
    @Transactional
    public StoreResponse create(StoreRequest request) {
        Store store = storeMapper.toEntity(request);
        uniqueChecker.verify(storeRepository , store , "name" , store.getName());
        uniqueChecker.verify(storeRepository , store , "code" , store.getCode());
        uniqueChecker.verify(storeRepository , store , "phone" , store.getPhone());
        uniqueChecker.verify(storeRepository , store , "email" , store.getEmail());
        return storeMapper.toResponse(storeRepository.save(store));
    }

    @Override
    @Transactional
    public StoreResponse update(Long id, StoreRequest request) {
        Store store = findById(id);
        uniqueChecker.verify(storeRepository , store , "name" , store.getName());
        uniqueChecker.verify(storeRepository , store , "code" , store.getCode());
        uniqueChecker.verify(storeRepository , store , "phone" , store.getPhone());
        uniqueChecker.verify(storeRepository , store , "email" , store.getEmail());
        storeMapper.updateEntityFromRequest(request, store);
        return storeMapper.toResponse(storeRepository.save(store));
    }

    @Override
    public StoreResponse getStoreById(Long id) {
        return storeMapper.toResponse(findById(id));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        Store store = findById(id);
        store.setStatus(GeneralStatus.INACTIVE);
        storeRepository.save(store);
    }

    @Override
    public Store findById(Long id) {
        return  storeRepository.findById(id).orElseThrow(()->new ResourceNotFoundExecption("Store" , id));
    }


}