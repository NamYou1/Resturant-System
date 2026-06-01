package com.saranaresturantsystem.services.impl.inventory;
import com.saranaresturantsystem.dto.request.inventory.AdjustmentItemRequest;
import com.saranaresturantsystem.dto.request.inventory.AdjustmentRequest;
import com.saranaresturantsystem.dto.response.inventory.AdjustmentResponse;
import com.saranaresturantsystem.entities.inventory.Adjustment;
import com.saranaresturantsystem.entities.inventory.AdjustmentItem;
import com.saranaresturantsystem.entities.inventory.Store;
import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.inventory.AdjustmentMapper;
import com.saranaresturantsystem.repositories.inventory.AdjustmentRepository;
import com.saranaresturantsystem.repositories.inventory.StoreRepository;
import com.saranaresturantsystem.repositories.product.ProductRepository;
import com.saranaresturantsystem.services.AdjustmentService;
import com.saranaresturantsystem.services.StockService;
import com.saranaresturantsystem.services.TransactionService;
import com.saranaresturantsystem.enums.SaleStatus;
import com.saranaresturantsystem.enums.TransactionType;
import com.saranaresturantsystem.specification.inventory.adjustments.AdjustmentFilter;
import com.saranaresturantsystem.specification.inventory.adjustments.AdjustmentSpec;
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
    private final StockService stockService;
    private final TransactionService transactionService;
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
        adjustment.setCreatedBy("1");
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

            if (qty.compareTo(BigDecimal.ZERO) > 0) {
                stockService.increaseStock(product.getId(), store.getId(), qty, cost);
            } else if (qty.compareTo(BigDecimal.ZERO) < 0) {
                stockService.decreaseStock(product.getId(), store.getId(), qty.abs());
            }
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
            BigDecimal qty = item.getQuantity();
            if (qty.compareTo(BigDecimal.ZERO) > 0) {
                stockService.decreaseStock(item.getProduct().getId(), existing.getStore().getId(), qty);
            } else if (qty.compareTo(BigDecimal.ZERO) < 0) {
                BigDecimal cost = item.getProduct().getCostPrice() != null ? item.getProduct().getCostPrice() : BigDecimal.ZERO;
                stockService.increaseStock(item.getProduct().getId(), existing.getStore().getId(), qty.abs(), cost);
            }
        }

        // 4. Soft delete
        existing.setStatus(StatusType.INACTIVE);
        adjustmentRepository.save(existing);
    }

    private void saveTransaction(Store store, AdjustmentItem item, Adjustment adjustment) {
        TransactionType type = item.getQuantity().compareTo(BigDecimal.ZERO) >= 0 
                ? TransactionType.ADJUSTMENT_IN 
                : TransactionType.ADJUSTMENT_OUT;

        transactionService.logStockMovement(
                type,
                adjustment.getId(),
                adjustment.getReferenceNo(),
                item.getProduct().getId(),
                store.getId(),
                item.getUnit() != null ? item.getUnit().getId() : null,
                item.getQuantity().abs(),
                item.getUnitQuantity(),
                item.getRealUnitCost() != null ? item.getRealUnitCost() : BigDecimal.ZERO,
                SaleStatus.COMPLETED,
                adjustment.getCreatedBy() != null ? adjustment.getCreatedBy() : "1"
        );
    }
}
