package com.saranaresturantsystem.services.impl;
import com.saranaresturantsystem.dto.request.SaleItemRequest;
import com.saranaresturantsystem.dto.request.SaleRequest;
import com.saranaresturantsystem.dto.response.SaleResponse;
import com.saranaresturantsystem.entities.*;
import com.saranaresturantsystem.mappers.SaleMapper;
import com.saranaresturantsystem.repositories.*;
import com.saranaresturantsystem.services.SalesService;
import com.saranaresturantsystem.specification.sales.SaleFilter;
import com.saranaresturantsystem.specification.sales.SaleSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class SalesServiceImp implements SalesService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final ProductStoreQtyRepository stockRepository;
    private final TransactionRepository transactionRepository;
    private final SaleMapper saleMapper;
    @Override
    @Transactional
    public SaleResponse createSale(SaleRequest request) {
        Integer lastNo = saleRepository.findMaxNo();
        int nextNo = (lastNo != null) ? lastNo + 1 : 1;
        String formattedNo = "POS-" + String.format("%05d", nextNo);
        Sale sale = saleMapper.toEntity(request);
        sale.setNo(nextNo);
        sale.setDate(LocalDateTime.now());
        sale.setDeleteFlag(0);
        sale.setCreatedBy(request.getSellerId() != null ? request.getSellerId().intValue() : null);
        sale.setStoreId(request.getStoreId() != null ? request.getStoreId() : 1);
        sale.setSaleStatus("completed");
        if (request.getHoldRef() == null || request.getHoldRef().isEmpty()) {
            sale.setHoldRef(formattedNo);
        } else {
            sale.setHoldRef(request.getHoldRef());
        }
        List<SaleItem> items = new ArrayList<>();
        BigDecimal totalQty = BigDecimal.ZERO;
        for (SaleItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId().longValue())
                    .orElseThrow(() -> new RuntimeException("រកមិនឃើញផលិតផល ID: " + itemReq.getProductId()));
            SaleItem item = buildSaleItem(sale, product, itemReq);
            items.add(item);
            totalQty = totalQty.add(itemReq.getQuantity());
            decreaseInventory(product, sale.getStoreId(), itemReq.getQuantity());
        }
        sale.setItems(items);
        sale.setTotalQuantity(totalQty);
        sale.setTotalItems(items.size());
        calculateSaleTotals(sale);
        BigDecimal grandTotal = sale.getGrandTotal();
        BigDecimal paidAmount = sale.getPaid() != null ? sale.getPaid() : BigDecimal.ZERO;
        if (paidAmount.compareTo(BigDecimal.ZERO) <= 0) {
            sale.setStatus("due");
        } else if (paidAmount.compareTo(grandTotal) >= 0) {
            sale.setStatus("paid");
        } else {
            sale.setStatus("partial");
        }
        Sale saved = saleRepository.save(sale);
        for (SaleItem item : saved.getItems()) {
            saveSaleTransaction(saved, item, request.getSellerId(), formattedNo, nextNo);
        }
        SaleResponse response = saleMapper.toResponse(saved);
        response.setReferenceNo(formattedNo);
        return response;
    }
    private SaleItem buildSaleItem(Sale sale, Product product, SaleItemRequest itemReq) {
        SaleItem item = new SaleItem();
        item.setSale(sale);
        item.setProductId(itemReq.getProductId());
        item.setProductName(product.getName());
        item.setProductCode(product.getCode());
        item.setQuantity(itemReq.getQuantity());
        item.setUnitPrice(itemReq.getUnitPrice());
        item.setNetUnitPrice(itemReq.getUnitPrice());
        item.setRealUnitPrice(itemReq.getUnitPrice());
        item.setCost(product.getCost() != null ? product.getCost() : BigDecimal.ZERO);
        BigDecimal disc = itemReq.getItemDiscount() != null ? itemReq.getItemDiscount() : BigDecimal.ZERO;
        item.setItemDiscount(disc);
        BigDecimal subtotal = itemReq.getQuantity().multiply(itemReq.getUnitPrice()).subtract(disc);
        item.setSubtotal(subtotal);
        return item;
    }
    private void decreaseInventory(Product product, Integer storeId, BigDecimal qty) {
        ProductStoreQty stock = stockRepository.findByProductIdAndStoreId(product.getId(), storeId)
                .orElseThrow(() -> new RuntimeException("ផលិតផល " + product.getName() + " មិនមានក្នុងស្តុកឃ្លាំងនេះទេ"));
        if (stock.getQuantity().compareTo(qty) < 0) {
            throw new RuntimeException("ស្តុកមិនគ្រប់គ្រាន់សម្រាប់: " + product.getName());
        }
        stock.setQuantity(stock.getQuantity().subtract(qty));
        stockRepository.save(stock);
    }
    private void saveSaleTransaction(Sale sale, SaleItem item, Long sellerId, String formattedNo, int nextNo) {
        Transaction tran = new Transaction();
        tran.setStoreId(sale.getStoreId());
        tran.setProductId(item.getProductId().longValue());
        tran.setNo(nextNo);
        tran.setReferenceNo(formattedNo);
        tran.setQuantity(item.getQuantity());
        tran.setTotalCost(item.getSubtotal());
        tran.setTotalValue(item.getUnitPrice());
        if (sale.getId() != null) {
            tran.setSaleId(sale.getId().intValue());
        }
        tran.setType("OUT");
        tran.setStatus("sale");
        tran.setTranDate(LocalDateTime.now());
        tran.setDate(LocalDateTime.now());
        if (sellerId != null) {
            tran.setCreateBy(sellerId.intValue());
        }
        transactionRepository.save(tran);
    }
    private void calculateSaleTotals(Sale sale) {
        BigDecimal total = sale.getItems().stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        sale.setTotal(total);
        BigDecimal orderDisc = sale.getOrderDiscount() != null ? sale.getOrderDiscount() : BigDecimal.ZERO;
        sale.setGrandTotal(total.subtract(orderDisc));
    }
    @Override
    public Page<SaleResponse> getList(Map<String, String> params) {
        SaleFilter filter = new SaleFilter();
        if (params.get("customerName") != null) filter.setCustomerName(params.get("customerName"));
        if (params.get("storeId") != null && !params.get("storeId").isEmpty()) {
            filter.setStoreId(Integer.parseInt(params.get("storeId")));
        }
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 0;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : 10;
        return saleRepository.findAll(SaleSpec.filter(filter), PageRequest.of(page, size))
                .map(saleMapper::toResponse);
    }
    @Override
    public SaleResponse findById(Long id) {
        return saleRepository.findById(id)
                .filter(s -> s.getDeleteFlag() == 0)
                .map(saleMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទិន្នន័យលក់"));
    }
    @Override
    @Transactional
    public void delete(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទិន្នន័យ"));
        sale.setDeleteFlag(1);
        saleRepository.save(sale);
    }
}