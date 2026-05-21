package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.dto.request.*;
import com.saranaresturantsystem.dto.request.PurchaseRequest;
import com.saranaresturantsystem.dto.response.PurchaseResponse;
import com.saranaresturantsystem.entities.*;
import com.saranaresturantsystem.entities.status.PaymentStatus;
import com.saranaresturantsystem.entities.status.PurchaseStatus;
import com.saranaresturantsystem.entities.status.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.PurchaseMapper;
import com.saranaresturantsystem.repositories.*;
import com.saranaresturantsystem.services.ProductService;
import com.saranaresturantsystem.services.PurchasesService;
import com.saranaresturantsystem.services.StoreService;
import com.saranaresturantsystem.specification.purchases.PurchaseFilter;
import com.saranaresturantsystem.specification.purchases.PurchaseSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class PurchasesServiceImp implements PurchasesService {

    private final PurchaseRepository purchaseRepository;
    private final ProductStoreQtyRepository stockRepository;
    private final TransactionRepository transactionRepository;
    private final PurchaseMapper purchaseMapper;
    private  final StoreService storeService ;
    private  final ProductService productService ;
    private  final ObjectMapper objectMapper;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        Purchase purchase = purchaseMapper.toEntity(request);
        purchase.setCreatedBy(request.getSellerId().intValue());
        purchase.setOrderDiscount(request.getOrderDiscount() != null ? BigDecimal.valueOf(request.getOrderDiscount()) : BigDecimal.ZERO);
        Store store = storeService.findById(request.getStoreId());
        List<PurchaseItem> items = new ArrayList<>();
        for (PurchaseItemRequest itemReq : request.getItems()) {
            Product product = productService.getProductById(itemReq.getProductId());
            PurchaseItem item = purchaseMapper.toItemEntity(itemReq);
            item.setPurchase(purchase);
            item.setProduct(product);
            item.setUnit(product.getUnit());
            item.setStoreId(request.getStoreId());
            BigDecimal itemDisc = itemReq.getTotalDiscount() != null ? BigDecimal.valueOf(itemReq.getTotalDiscount()) : BigDecimal.ZERO;
            item.setTotalDiscount(itemDisc);
            // Subtotal = (Qty * Cost)
            item.setSubtotal(item.getQuantity().multiply(item.getCostPrice()).subtract(itemDisc));

            items.add(item);
            // Inventory & Transaction
            updateInventory(product, request.getStoreId(), item.getQuantity(), item.getCostPrice());
        }
        purchase.setItems(items);
        purchase.calculateTotals();
        // 4. Save and record transactions
        Purchase saved = purchaseRepository.save(purchase);
        // Record transactions after we have a valid Purchase ID
        for (PurchaseItem item : saved.getItems()) {
            saveTransaction(store, item.getProduct(), item.getQuantity(), item.getSubtotal(), saved, request.getSellerId());
        }
        return purchaseMapper.toResponse(saved);
    }

    @Override
    public Page<PurchaseResponse> getList(Map<String, String> params) {
        PurchaseFilter filter = objectMapper.convertValue(params, PurchaseFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<Purchase> spec = PurchaseSpec.filter(filter);
        return  purchaseRepository.findAll(spec, pageable).map(purchaseMapper::toResponse);
    }

    @Override
    public PurchaseResponse findById(Long id) {
        return purchaseMapper.toResponse(getById(id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Purchase purchase = getById(id);
        purchase.setStatus(StatusType.ACTIVE);
        purchaseRepository.save(purchase);
    }

    @Override
    public Purchase getById(Long id) {
        return purchaseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Purchase" , id));
    }

    private void updateInventory(Product product, Long storeId, BigDecimal qty, BigDecimal cost) {
        ProductStoreQty stock = stockRepository.findByProductIdAndStoreId(product.getId(), storeId)
                .orElse(new ProductStoreQty());
        if (stock.getId() == null) {
            stock.setProduct(product);
            stock.setStoreId(storeId);
            stock.setQuantity(qty);
        } else {
            stock.setQuantity(stock.getQuantity().add(qty));
        }
        stock.setPrice(cost);
        stockRepository.save(stock);
    }

    private void saveTransaction(Store store, Product product, BigDecimal qty, BigDecimal total, Purchase purchase, Long sellerId) {
        Transaction tran = new Transaction();
        tran.setStore(store);
        tran.setStoreId(store.getId().intValue());
        tran.setProduct(product);
        tran.setProductId(product.getId());
        tran.setUnit(product.getUnit());
        tran.setQuantity(qty);
        tran.setType("IN");
        tran.setStatus("purchase");
        tran.setTotalCost(total);
        tran.setPurchaseId(purchase.getId().intValue());
        tran.setDate(LocalDateTime.now());
        tran.setTranDate(LocalDateTime.now());
        tran.setCreateBy(sellerId.intValue());
        transactionRepository.save(tran);
    }
}