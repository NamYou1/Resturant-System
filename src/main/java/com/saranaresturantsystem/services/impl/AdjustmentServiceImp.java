package com.saranaresturantsystem.services.impl;
import com.saranaresturantsystem.dto.request.AdjustmentItemRequest;
import com.saranaresturantsystem.dto.request.AdjustmentRequest;
import com.saranaresturantsystem.dto.response.AdjustmentResponse;
import com.saranaresturantsystem.entities.*;
import com.saranaresturantsystem.entities.status.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.AdjustmentMapper;
import com.saranaresturantsystem.repositories.*;
import com.saranaresturantsystem.services.AdjustmentService;
import com.saranaresturantsystem.specification.adjustments.AdjustmentFilter;
import com.saranaresturantsystem.specification.adjustments.AdjustmentSpec;
import com.saranaresturantsystem.utils.PageUtil;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdjustmentServiceImp implements AdjustmentService {
    private final AdjustmentRepository adjustmentRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private  final ObjectMapper objectMapper ;
    private final ProductStoreQtyRepository stockRepository;
    private final TransactionRepository transactionRepository;
    private final AdjustmentMapper  adjustmentMapper;
    @Override
    public Page<AdjustmentResponse> getList(Map<String, String> params) {

        AdjustmentFilter filter = objectMapper.convertValue(params, AdjustmentFilter.class);

        Pageable pageable = PageUtil.fromParams(params);
        Specification<Adjustment> spec = AdjustmentSpec.filter(filter);

        return adjustmentRepository.findAll(spec, pageable)
                .map(adjustmentMapper::toResponse);
    }

    @Override
    public AdjustmentResponse findById(@Positive Long id) {
        Adjustment adjustment = adjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adjustment", id));
        return adjustmentMapper.toResponse(adjustment);
    }

    @Override
    @Transactional
    public AdjustmentResponse createAdjustment(AdjustmentRequest request) {

        // 1. Validate Store
        Store store = storeRepository.findById(Long.valueOf(request.getStoreId()))
                .orElseThrow(() -> new ResourceNotFoundException("Store", Long.valueOf(request.getStoreId())));
        // 2. Build Adjustment manually (don't rely on mapper for FK fields)
        Adjustment adjustment = new Adjustment();
        adjustment.setReferenceNo(request.getReferenceNo());
        adjustment.setNote(request.getNote());
        adjustment.setFile(request.getFile());
        adjustment.setStore(store);
        adjustment.setDate(LocalDateTime.now());
//        adjustment.setDeleteFlag(1);
        adjustment.setDeleteBy(1);
        adjustment.setCreateBy(1);
        adjustment.setStatus(StatusType.ACTIVE);
        // 3. Process Items
        List<AdjustmentItem> items = new ArrayList<>();
        BigDecimal totalAdjustmentValue = BigDecimal.ZERO;

        for (AdjustmentItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", itemReq.getProductId()));

            BigDecimal qty = BigDecimal.valueOf(itemReq.getQuantity());
            BigDecimal cost = product.getCostPrice() != null ? product.getCostPrice() : BigDecimal.ZERO;

            AdjustmentItem item = new AdjustmentItem();
            item.setAdjustment(adjustment);
            item.setProduct(product);
            item.setQuantity(qty);
            item.setSubtotal(qty.multiply(cost));
            item.setRealUnitCost(cost);
            item.setQuantityPerUnit(BigDecimal.ONE);
            item.setUnitQuantity(qty);
            item.setUnit(product.getUnit());
            items.add(item);
            totalAdjustmentValue = totalAdjustmentValue.add(item.getSubtotal());

            updateInventory(product, store.getId(), qty);
        }

        adjustment.setItems(items);
        adjustment.setTotal(totalAdjustmentValue);

        // 4. Save
        Adjustment saved = adjustmentRepository.save(adjustment);

        // 5. Save Transactions
        for (AdjustmentItem item : items) {
            saveTransaction(store, item, saved);
        }

        return adjustmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteAdjustment(Long id) {
        // 1. Find existing adjustment
        Adjustment existing = adjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adjustment", id));

        // 2. Reverse stock for each item
        for (AdjustmentItem item : existing.getItems()) {
            reverseInventory(item.getProduct(), existing.getStore().getId(), item.getQuantity());
        }

        // 4. Soft delete
        existing.setStatus(StatusType.INACTIVE);
        adjustmentRepository.save(existing);
    }


    // Reverse stock (subtract previously added quantity)
    private void reverseInventory(Product product, Long storeId, BigDecimal qty) {
        ProductStoreQty stock = stockRepository.findByProductIdAndStoreId(product.getId(), storeId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Stock", product.getId()));

        // Subtract the old quantity
        stock.setQuantity(stock.getQuantity().subtract(qty));
        stockRepository.save(stock);
    }

    private void updateInventory(Product product, Long storeId, BigDecimal qty) {
    ProductStoreQty stock = stockRepository.findByProductIdAndStoreId(product.getId(), storeId.intValue())
            .orElseGet(() -> {
                ProductStoreQty newStock = new ProductStoreQty();
                newStock.setProduct(product);
                newStock.setStoreId(storeId);
                newStock.setQuantity(BigDecimal.ZERO);
                newStock.setPrice(product.getCostPrice() != null ? product.getCostPrice() : BigDecimal.ZERO);
                return newStock;
            });

    stock.setQuantity(stock.getQuantity().add(qty));
    stockRepository.save(stock);
}

    private void saveTransaction(Store store, AdjustmentItem item, Adjustment adjustment) {
        Transaction tran = new Transaction();
        tran.setStoreId(store.getId().intValue());
        tran.setProductId(item.getProduct().getId());
        tran.setAdjustId(adjustment.getId().intValue());
        tran.setQuantity(item.getQuantity());
        tran.setType(item.getQuantity().compareTo(BigDecimal.ZERO) >= 0 ? "IN" : "OUT");
        tran.setStatus("adjustment");
        tran.setTotalCost(item.getSubtotal());
        tran.setCreateBy(adjustment.getCreateBy());
        tran.setDate(LocalDateTime.now());
        tran.setTranDate(LocalDateTime.now());

        transactionRepository.save(tran);
    }
}
