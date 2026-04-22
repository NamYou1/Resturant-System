package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.dto.request.*;
import com.saranaresturantsystem.dto.request.PurchaseRequest;
import com.saranaresturantsystem.dto.response.PurchaseResponse;
import com.saranaresturantsystem.entities.*;
import com.saranaresturantsystem.mappers.PurchaseMapper;
import com.saranaresturantsystem.repositories.*;
import com.saranaresturantsystem.services.PurchasesService;
import com.saranaresturantsystem.specification.purchases.PurchaseFilter;
import com.saranaresturantsystem.specification.purchases.PurchaseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchasesServiceImp implements PurchasesService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final StoreRepository storeRepository;
    private final ProductStoreQtyRepository stockRepository;
    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;
    private final PurchaseMapper purchaseMapper;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        // 1. Validate Entities
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // 2. Initialize Purchase
        Purchase purchase = purchaseMapper.toEntity(request);
        purchase.setStore(store);
        purchase.setSupplier(supplier);
        purchase.setSeller(seller);
        purchase.setCreatedBy(seller.getId().intValue());
        purchase.setPurchasesStatus("received");
        purchase.setPaymentStatus("Paid");
        purchase.setOrderDiscount(request.getOrderDiscount() != null ? BigDecimal.valueOf(request.getOrderDiscount()) : BigDecimal.ZERO);

        // 3. Process Items
        List<PurchaseItem> items = new ArrayList<>();
        for (PurchaseItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));

            PurchaseItem item = new PurchaseItem();
            item.setPurchase(purchase);
            item.setProduct(product);
            item.setUnit(product.getUnit());
            item.setStoreId(store.getId().intValue());

            BigDecimal qty = BigDecimal.valueOf(itemReq.getQuantity());
            BigDecimal cost = BigDecimal.valueOf(itemReq.getCost());
            BigDecimal itemDisc = itemReq.getTotalDiscount() != null ? BigDecimal.valueOf(itemReq.getTotalDiscount()) : BigDecimal.ZERO;

            item.setQuantity(qty);
            item.setCost(cost);
            item.setTotalDiscount(itemDisc);
            // Subtotal = (Qty * Cost)
            item.setSubtotal(qty.multiply(cost));

            items.add(item);

            // Inventory & Transaction
            updateInventory(product, store.getId(), qty, cost);
        }

        purchase.setItems(items);
        purchase.calculateTotals();

        // 4. Save and record transactions
        Purchase saved = purchaseRepository.save(purchase);

        // Record transactions after we have a valid Purchase ID
        for (PurchaseItem item : saved.getItems()) {
            saveTransaction(store, item.getProduct(), item.getQuantity(), item.getSubtotal(), saved, seller.getId());
        }

        return purchaseMapper.toResponse(saved);
    }


    @Override
    public Page<PurchaseResponse> getList(Map<String, String> params) {
        // 1. Setup Filter Object
        PurchaseFilter filter = new PurchaseFilter();
        filter.setReference(params.get("reference"));
        filter.setStatus(params.get("status"));

        if (params.get("supplierId") != null && !params.get("supplierId").isEmpty()) {
            filter.setSupplierId(Long.parseLong(params.get("supplierId")));
        }
        if (params.get("storeId") != null && !params.get("storeId").isEmpty()) {
            filter.setStoreId(Long.parseLong(params.get("storeId")));
        }

        // 2. Setup Pagination
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 0;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : 10;

        // 3. Execute with Specification (Handles the WHERE delete_flag = 0 logic)
        Page<Purchase> purchasePage = purchaseRepository.findAll(
                PurchaseSpec.filter(filter),
                PageRequest.of(page, size)
        );

        return purchasePage.map(purchaseMapper::toResponse);
    }

    @Override
    public PurchaseResponse findById(Long id) {
        return purchaseRepository.findById(id)
                .filter(p -> p.getDeleteFlag() == 0) // Only return if not soft-deleted
                .map(purchaseMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទិន្នន័យទិញចូល (Purchase Not Found)"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទិន្នន័យសម្រាប់លុប"));

        // Perform Soft Delete
        purchase.setDeleteFlag(1);
        purchaseRepository.save(purchase);
    }

    private void updateInventory(Product product, Long storeId, BigDecimal qty, BigDecimal cost) {
        ProductStoreQty stock = stockRepository.findByProductIdAndStoreId(product.getId(), storeId.intValue())
                .orElse(new ProductStoreQty());

        if (stock.getId() == null) {
            stock.setProduct(product);
            stock.setStoreId(storeId.intValue());
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