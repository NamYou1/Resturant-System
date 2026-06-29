package com.saranaresturantsystem.services.impl.purchases;

import com.saranaresturantsystem.dto.request.purchases.PurchaseItemRequest;
import com.saranaresturantsystem.dto.request.purchases.PurchaseRequest;
import com.saranaresturantsystem.dto.response.purchases.PurchaseResponse;
import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.entities.purchases.Purchase;
import com.saranaresturantsystem.entities.purchases.PurchaseItem;
import com.saranaresturantsystem.enums.PaymentStatus;
import com.saranaresturantsystem.enums.PurchaseStatus;
import com.saranaresturantsystem.enums.SaleStatus;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.enums.TransactionType;
import com.saranaresturantsystem.execption.ApiException;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.purchases.PurchaseMapper;
import com.saranaresturantsystem.repositories.purchases.PurchaseRepository;
import com.saranaresturantsystem.services.*;
import com.saranaresturantsystem.specification.users.purchases.PurchaseFilter;
import com.saranaresturantsystem.specification.users.purchases.PurchaseSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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
public class PurchasesServiceImp implements PurchasesService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final ProductService productService;
    private final StockService stockService;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    // ── CREATE ───────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public PurchaseResponse create(PurchaseRequest request) {
        Purchase purchase = purchaseMapper.toEntity(request);
        purchase.setPurchasesStatus(PurchaseStatus.ORDERED);
        purchase.setPaymentStatus(PaymentStatus.PENDING);
        purchase.setStatus(StatusType.ACTIVE);
        purchase.setCreatedBy(request.getSellerId() != null ? request.getSellerId().toString() : null);
        purchase.setOrderDiscount(request.getOrderDiscount() != null ?
                BigDecimal.valueOf(request.getOrderDiscount()) : BigDecimal.ZERO);
        List<PurchaseItem> items = new ArrayList<>();
        for (PurchaseItemRequest itemReq : request.getItems()) {
            Product product = productService.getProductById(itemReq.getProductId());
            PurchaseItem item = purchaseMapper.toItemEntity(itemReq);
            item.setPurchase(purchase);
            item.setProduct(product);
            item.setUnit(product.getUnit());
//            item.setStoreId(request.getStoreId());
            BigDecimal itemDisc = itemReq.getTotalDiscount() != null ? BigDecimal.valueOf(itemReq.getTotalDiscount()) : BigDecimal.ZERO;
            item.setTotalDiscount(itemDisc);
            item.setSubtotal(item.getQuantity().multiply(item.getCostPrice()).subtract(itemDisc));
            items.add(item);
        }
        purchase.setItems(items);
        purchase.calculateTotals();
        return purchaseMapper.toResponse(purchaseRepository.save(purchase));
    }
    // ── GET ALL ──────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseResponse> getAll(Map<String, String> params) {
        PurchaseFilter filter = objectMapper.convertValue(params, PurchaseFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<Purchase> spec = PurchaseSpec.filter(filter);
        return purchaseRepository.findAll(spec, pageable).map(purchaseMapper::toResponse);
    }

    // ── GET BY ID ────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getById(Long id) {
        return purchaseMapper.toResponse(findPurchaseById(id));
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public PurchaseResponse update(Long id, PurchaseRequest request, String updatedBy) {
        Purchase purchase = findPurchaseById(id);
//        if (purchase.getPurchasesStatus() != PurchaseStatus.ORDERED) {
//            throw new ApiException(HttpStatus.BAD_REQUEST, "Only ORDERED purchases can be updated");
//        }
        purchase.setNote(request.getNote());
        if (request.getDate() != null) {
            purchase.setDate(request.getDate());
        }
        purchase.setUpdatedBy(updatedBy);
        purchase.setUpdatedAt(LocalDateTime.now());
        return purchaseMapper.toResponse(purchaseRepository.save(purchase));
    }

    // ── APPROVE ──────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public PurchaseResponse approve(Long id, String updatedBy) {
        Purchase purchase = findPurchaseById(id);
        if (purchase.getPurchasesStatus() != PurchaseStatus.ORDERED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only ORDERED purchases can be approved");
        }

        purchase.setPurchasesStatus(PurchaseStatus.APPROVED);
        purchase.setUpdatedBy(updatedBy);
        purchase.setUpdatedAt(LocalDateTime.now());

        return purchaseMapper.toResponse(purchaseRepository.save(purchase));
    }

    // ── COMPLETE ─────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public PurchaseResponse complete(Long id, String updatedBy) {
        Purchase purchase = findPurchaseById(id);
        if (purchase.getPurchasesStatus() != PurchaseStatus.APPROVED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only APPROVED purchases can be completed");
        }

        Long storeId = getStoreIdOrThrow(purchase);

        for (PurchaseItem item : purchase.getItems()) {
            // Increase stock
            stockService.increaseStock(item.getProduct().getId(), storeId, item.getQuantity(), item.getCostPrice());

            // Log transaction
            transactionService.logStockMovement(
                    TransactionType.PURCHASE,
                    purchase.getId(),
                    purchase.getReference(),
                    item.getProduct().getId(),
                    storeId,
                    item.getUnit() != null ? item.getUnit().getId() : null,
                    item.getQuantity(),
                    item.getQuantity(),
                    item.getCostPrice(),
                    SaleStatus.COMPLETED,
                    updatedBy
            );
        }

        purchase.setPurchasesStatus(PurchaseStatus.COMPLETED);
        purchase.setPaymentStatus(PaymentStatus.PAID);
        purchase.setUpdatedBy(updatedBy);
        purchase.setUpdatedAt(LocalDateTime.now());

        return purchaseMapper.toResponse(purchaseRepository.save(purchase));
    }

    // ── CANCEL ───────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public PurchaseResponse cancel(Long id, String updatedBy) {
        Purchase purchase = findPurchaseById(id);
        if (purchase.getPurchasesStatus() == PurchaseStatus.CANCELLED
                || purchase.getPurchasesStatus() == PurchaseStatus.RETURNED
                || purchase.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Purchase already cancelled/refunded");
        }
        // Reverse stock if the purchase was already completed
        if (purchase.getPurchasesStatus() == PurchaseStatus.COMPLETED) {
            reverseStockAndSaveTransactions(purchase, updatedBy);
        }
        purchase.setPurchasesStatus(PurchaseStatus.RETURNED);
        purchase.setPaymentStatus(PaymentStatus.REFUNDED);
        purchase.setUpdatedBy(updatedBy);
        purchase.setUpdatedAt(LocalDateTime.now());
        return purchaseMapper.toResponse(purchaseRepository.save(purchase));
    }

    // ── DELETE (soft) ────────────────────────────────────────────────────────
    @Override
    @Transactional
    public void delete(Long id, String deletedBy) {
        Purchase purchase = findPurchaseById(id);
        if (purchase.getStatus() == StatusType.INACTIVE) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Purchase already deleted");
        }
        if (purchase.getPurchasesStatus() == PurchaseStatus.COMPLETED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot delete completed purchase");
        }

        purchase.setStatus(StatusType.INACTIVE);
        purchase.setDeletedBy(deletedBy);
        purchase.setDeletedAt(LocalDateTime.now());
        purchase.setUpdatedBy(deletedBy);
        purchase.setUpdatedAt(LocalDateTime.now());
        purchaseRepository.save(purchase);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private Purchase findPurchaseById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase", id));
    }

    private Long getStoreIdOrThrow(Purchase purchase) {
        if (purchase.getStore() == null || purchase.getStore().getId() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Purchase store is required");
        }
        return purchase.getStore().getId();
    }

    private void reverseStockAndSaveTransactions(Purchase purchase, String updatedBy) {
        Long storeId = getStoreIdOrThrow(purchase);
        for (PurchaseItem item : purchase.getItems()) {
            stockService.decreaseStock(item.getProduct().getId(), storeId, item.getQuantity());

            transactionService.logStockMovement(
                    TransactionType.ADJUSTMENT_OUT,
                    purchase.getId(),
                    purchase.getReference(),
                    item.getProduct().getId(),
                    storeId,
                    item.getUnit() != null ? item.getUnit().getId() : null,
                    item.getQuantity(),
                    item.getQuantity(),
                    item.getCostPrice(),
                    SaleStatus.CANCELLED,
                    updatedBy
            );
        }
    }
}