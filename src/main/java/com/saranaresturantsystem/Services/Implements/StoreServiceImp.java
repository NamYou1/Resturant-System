package com.saranaresturantsystem.Services.Implements;

import com.saranaresturantsystem.DTO.Request.StoreRequest;
import com.saranaresturantsystem.DTO.Response.StoreResponse;
import com.saranaresturantsystem.Enities.Store;
import com.saranaresturantsystem.Mappers.StoreMapper;
import com.saranaresturantsystem.Repositories.StoreRepository;
import com.saranaresturantsystem.Services.Interfaces.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImp implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Override
    @Transactional
    public StoreResponse create(StoreRequest request) {
        Store store = storeMapper.toEntity(request);
        return storeMapper.toResponse(storeRepository.save(store));
    }

    @Override
    @Transactional
    public StoreResponse update(Long id, StoreRequest request) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        storeMapper.updateEntityFromRequest(request, store);
        return storeMapper.toResponse(storeRepository.save(store));
    }

    @Override
    public StoreResponse findById(Long id) {
        return storeRepository.findById(id)
                .map(storeMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Store not found"));
    }

    @Override
    public List<StoreResponse> findAll() {
        return storeMapper.toResponseList(storeRepository.findAll());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        storeRepository.deleteById(id);
    }
}