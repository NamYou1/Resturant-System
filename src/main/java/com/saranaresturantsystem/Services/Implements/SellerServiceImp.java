package com.saranaresturantsystem.Services.Implements;

import com.saranaresturantsystem.DTO.Request.SellerRequest;
import com.saranaresturantsystem.DTO.Response.SellerResponse;
import com.saranaresturantsystem.Enities.Seller;
import com.saranaresturantsystem.Mappers.SellerMapper;
import com.saranaresturantsystem.Repositories.SellerRepository;
import com.saranaresturantsystem.Services.SellerService;
import com.saranaresturantsystem.Specification.Seller.SellerFilter;
import com.saranaresturantsystem.Specification.Seller.SellerSpec;
import com.saranaresturantsystem.Utils.GloblePagination; // ប្រាកដថាអ្នកមាន Class នេះសម្រាប់ទាញ Key "pageNumber" និង "pageSize"
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
                ? Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER)) : 0;

        int pageSize = params.containsKey(GloblePagination.DEFAULT_PAGE_LIMIT)
                ? Integer.parseInt(params.get(GloblePagination.DEFAULT_PAGE_LIMIT)) : 10;

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
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller with ID " + id + " not found"));

        // ធ្វើការ Update field ដែលមានស្រាប់ក្នុង Entity តាមរយៈ Request
        sellerMapper.updateSellerFromRequest(request, seller);

        return sellerMapper.toSellerResponse(sellerRepository.save(seller));
    }

    @Override
    public SellerResponse findById(Long id) {
        return sellerRepository.findById(id)
                .map(sellerMapper::toSellerResponse)
                .orElseThrow(() -> new RuntimeException("Seller with ID " + id + " not found"));
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
        // ប្រសិនបើចង់ធ្វើ Soft Delete ដូច Category (status = "ina") អាចប្តូរនៅទីនេះ
        if (!sellerRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Seller not found");
        }
        sellerRepository.deleteById(id);
    }
}