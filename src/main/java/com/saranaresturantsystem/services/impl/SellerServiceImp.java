package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.dto.request.SellerRequest;
import com.saranaresturantsystem.dto.response.SellerResponse;
import com.saranaresturantsystem.entities.Seller;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.mappers.SellerMapper;
import com.saranaresturantsystem.repositories.SellerRepository;
import com.saranaresturantsystem.services.SellerService;
import com.saranaresturantsystem.specification.peoples.seller.SellerFilter;
import com.saranaresturantsystem.specification.peoples.seller.SellerSpec;
import com.saranaresturantsystem.utils.GloblePagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    /**
     * ទាញយកបញ្ជី Seller ជាមួយ Pagination និង Filter
     */
    @Override
    public Page<SellerResponse> getList(Map<String, String> params) {
        // បំប្លែង params ទៅជា Filter Object (ឧទាហរណ៍៖ ?name=ABC&phone=012...)
        SellerFilter filter = objectMapper.convertValue(params, SellerFilter.class);

        // ទាញយកលេខទំព័រ និងទំហំទំព័រពី params (Default គឺ page 0, size 10)
        int pageNumber = params.containsKey(GloblePagination.PAGE_NUMBER)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER))
                : GloblePagination.DEFAULT_PAGE_NUMBER;

        int pageSize = params.containsKey(GloblePagination.PAGE_LIMIT)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_LIMIT))
                : GloblePagination.DEFAULT_PAGE_LIMIT;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
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
        seller.setStatus(GeneralStatus.INACTIVE);
        sellerRepository.save(seller);
    }

    @Override
    public Seller getById(Long id) {
        return  sellerRepository.findById(id).orElseThrow(()->new ResourceNotFoundExecption("Seller",id));
    }
}