package com.saranaresturantsystem.services.impl.users;

import com.saranaresturantsystem.dto.request.sales.SellerRequest;
import com.saranaresturantsystem.dto.response.sales.SellerResponse;
import com.saranaresturantsystem.entities.sales.Seller;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.sales.SellerMapper;
import com.saranaresturantsystem.repositories.sales.SellerRepository;
import com.saranaresturantsystem.services.SellerService;
import com.saranaresturantsystem.specification.users.seller.SellerFilter;
import com.saranaresturantsystem.specification.users.seller.SellerSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerServiceImp implements SellerService {

    private final ObjectMapper objectMapper ;
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;


    @Override
    public Page<SellerResponse> getList(Map<String, String> params) {
        SellerFilter filter = objectMapper.convertValue(params, SellerFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<Seller> spec = SellerSpec.filterBy(filter);
        return sellerRepository.findAll(spec, pageable).map(sellerMapper::toSellerResponse);
    }

    @Override
    @Transactional
    public SellerResponse create(SellerRequest request) {
        Seller seller = sellerMapper.toSeller(request);
        return sellerMapper.toSellerResponse(sellerRepository.save(seller));
    }

    @Override
    @Transactional
    public SellerResponse update(Long id, SellerRequest request) {
        Seller seller= getById(id);
        sellerMapper.updateSellerFromRequest(request, seller);
        return sellerMapper.toSellerResponse(sellerRepository.save(seller));
    }

    @Override
    public SellerResponse findById(Long id) {
      return  sellerMapper.toSellerResponse(getById(id));
    }

    @Override
    public List<SellerResponse> findAll() {
        return sellerRepository.findAll().stream()
                .map(sellerMapper::toSellerResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Seller seller = getById(id);
        seller.setStatus(StatusType.INACTIVE);
        sellerRepository.save(seller);
    }
    @Override
    public Seller getById(Long id) {
        return  sellerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Seller",id));
    }
}