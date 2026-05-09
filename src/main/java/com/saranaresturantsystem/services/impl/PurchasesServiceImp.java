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
//        purchase.setPurchasesStatus(PurchaseStatus.RECEIVED);
//        purchase.setPaymentStatus(PaymentStatus.PAID);
        purchase.setOrderDiscount(request.getOrderDiscount() != null ? BigDecimal.valueOf(request.getOrderDiscount()) : BigDecimal.ZERO);
        Store store = storeService.findById(request.getStoreId());
        // 3. Process Items
        List<PurchaseItem> items = new ArrayList<>();
        for (PurchaseItemRequest itemReq : request.getItems()) {
            Product product = productService.getProductById(itemReq.getProductId());

            PurchaseItem item = purchaseMapper.toItemEntity(itemReq);
//            PurchaseItem item = new PurchaseItem();
            item.setPurchase(purchase);
            item.setProduct(product);
            item.setUnit(product.getUnit());
            item.setStoreId(request.getStoreId());

//            BigDecimal qty = BigDecimal.valueOf(itemReq.getQuantity());
//            BigDecimal cost = BigDecimal.valueOf(itemReq.getCostPrice());
            BigDecimal itemDisc = itemReq.getTotalDiscount() != null ? BigDecimal.valueOf(itemReq.getTotalDiscount()) : BigDecimal.ZERO;

//            item.setQuantity(qty);
//            item.setCostPrice(cost);
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
        int pageNumber = params.containsKey(PageUtil.PAGE_NUMBER)
                ? Integer.parseInt(params.get(PageUtil.PAGE_NUMBER))
                : PageUtil.DEFAULT_PAGE_SIZE;
        int pageSize = params.containsKey(PageUtil.PAGE_LIMIT)
                ? Integer.parseInt(params.get(PageUtil.PAGE_LIMIT))
                : PageUtil.DEFAULT_PAGE;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Specification<Purchase> spec = PurchaseSpec.filter(filter);
        return  purchaseRepository.findAll(spec, pageable).map(purchaseMapper::toResponse);

//        // 1. Setup Filter Object
//        PurchaseFilter filter = new PurchaseFilter();
//        filter.setReference(params.get("reference"));
//        filter.setStatus(params.get("status"));
//
//        if (params.get("supplierId") != null && !params.get("supplierId").isEmpty()) {
//            filter.setSupplierId(Long.parseLong(params.get("supplierId")));
//        }
//        if (params.get("storeId") != null && !params.get("storeId").isEmpty()) {
//            filter.setStoreId(Long.parseLong(params.get("storeId")));
//        }
//
//        // 2. Setup Pagination
//        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 0;
//        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : 10;
//
//        // 3. Execute with Specification (Handles the WHERE delete_flag = 0 logic)
//        Page<Purchase> purchasePage = purchaseRepository.findAll(
//                PurchaseSpec.filter(filter),
//                PageRequest.of(page, size)
//        );
//
//        return purchasePage.map(purchaseMapper::toResponse);
    }

    @Override
    public PurchaseResponse findById(Long id) {
        return purchaseMapper.toResponse(getById(id));
//        return purchaseRepository.findById(id)
//                .filter(p -> p.getDeleteFlag() == 0) // Only return if not soft-deleted
//                .map(purchaseMapper::toResponse)
//                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទិន្នន័យទិញចូល (Purchase Not Found)"));
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
        tran.setProduct(product);
        tran.setUnit(product.getUnit());
        tran.setQuantity(qty);
        tran.setType("IN");
        tran.setStatus("purchase");
        tran.setTotalCost(total);
        tran.setPurchaseId(purchase.getId().intValue());
        tran.setCreateBy(sellerId.intValue());
        transactionRepository.save(tran);
    }
}