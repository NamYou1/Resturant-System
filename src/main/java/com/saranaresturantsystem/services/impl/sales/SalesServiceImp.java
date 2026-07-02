package com.saranaresturantsystem.services.impl.sales;

import com.saranaresturantsystem.dto.request.sales.SaleItemRequest;
import com.saranaresturantsystem.dto.request.sales.SaleRequest;
import com.saranaresturantsystem.dto.response.sales.SaleResponse;
import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.entities.sales.Sale;
import com.saranaresturantsystem.entities.sales.SaleItem;
import com.saranaresturantsystem.mappers.sales.SaleMapper;
import com.saranaresturantsystem.repositories.sales.SaleItemRepository;
import com.saranaresturantsystem.repositories.sales.SaleRepository;
import com.saranaresturantsystem.services.ProductService;
import com.saranaresturantsystem.services.SalesService;
import com.saranaresturantsystem.enums.SaleStatus;
import com.saranaresturantsystem.enums.TransactionType;
import com.saranaresturantsystem.services.TransactionService;
import com.saranaresturantsystem.specification.sales.sale.SaleFilter;
import com.saranaresturantsystem.specification.sales.sale.SaleSpec;
import com.saranaresturantsystem.utils.PageUtil;
import com.saranaresturantsystem.services.StockService;
import com.saranaresturantsystem.execption.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesServiceImp implements SalesService {
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final StockService stockService;
    private final TransactionService transactionService;
    private final ProductService productService;
    private final SaleMapper saleMapper;
    private final ObjectMapper objectMapper ; 

      @Override
    public Page<SaleResponse> getAll(Map<String,String> params) {
        SaleFilter filter = objectMapper.convertValue(params, SaleFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
        Specification<Sale> spec = SaleSpec.filter(filter);
        Page<Sale> sales = saleRepository.findAll(spec, pageable);
        return sales.map(saleMapper::toResponse);
    }
    @Override
    @Transactional
    public SaleResponse create(SaleRequest request, String createdBy) {
        Integer lastNo = saleRepository.findMaxNo();
        int nextNo = (lastNo != null) ? lastNo + 1 : 1;
        String formattedNo = "POS-" + String.format("%05d", nextNo);
        Sale sale = saleMapper.toEntity(request);
        sale.setNo(nextNo);
        sale.setDate(LocalDateTime.now());
        sale.setDeleteFlag(0);
        sale.setStoreId(request.getStoreId() != null ? request.getStoreId() : 1);
        sale.setSaleStatus(SaleStatus.PENDING.name().toLowerCase());
        
        if (request.getHoldRef() == null || request.getHoldRef().isEmpty()) {
            sale.setHoldRef(formattedNo);
        } else {
            sale.setHoldRef(request.getHoldRef());
        }

        List<SaleItem> items = new ArrayList<>();
        BigDecimal totalQty = BigDecimal.ZERO;
        for (SaleItemRequest itemReq : request.getItems()) {
            Product product = productService.getProductById(itemReq.getProductId().longValue());
            SaleItem item = buildSaleItem(sale, product, itemReq);
            items.add(item);
            totalQty = totalQty.add(itemReq.getQuantity());
        }
        sale.setItems(items);
        sale.setTotalQuantity(totalQty);
        sale.setTotalItems(items.size());
        calculateSaleTotals(sale);

        BigDecimal grandTotal = sale.getGrandTotal();
        sale.setStatus("due");

        Sale saved = saleRepository.save(sale);
        SaleResponse response = saleMapper.toResponse(saved);
        response.setReferenceNo(formattedNo);
        return response;
    }

    @Override
    public SaleResponse getById(Long id) {
        return saleRepository.findById(id)
                .filter(s -> s.getDeleteFlag() == 0)
                .map(saleMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", id));
    }

  

    @Override
    @Transactional
    public SaleResponse update(Long id, SaleRequest request, String updatedBy) {
        Sale sale = findById(id);
        if (!SaleStatus.PENDING.name().equalsIgnoreCase(sale.getSaleStatus())) {
            throw new IllegalArgumentException("Only PENDING sales can be updated");
        }

        saleMapper.updateFromRequest(request, sale);

        // Delete existing items
        if (sale.getItems() != null) {
            saleItemRepository.deleteAll(sale.getItems());
            sale.getItems().clear();
        }

        List<SaleItem> items = new ArrayList<>();
        BigDecimal totalQty = BigDecimal.ZERO;
        for (SaleItemRequest itemReq : request.getItems()) {
            Product product = productService.getProductById(itemReq.getProductId().longValue());
            SaleItem item = buildSaleItem(sale, product, itemReq);
            items.add(item);
            totalQty = totalQty.add(itemReq.getQuantity());
        }
        sale.setItems(items);
        sale.setTotalQuantity(totalQty);
        sale.setTotalItems(items.size());
        calculateSaleTotals(sale);

        Sale saved = saleRepository.save(sale);
        return saleMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public SaleResponse approve(Long id, String updatedBy) {
        Sale sale = findById(id);
        if (!SaleStatus.PENDING.name().equalsIgnoreCase(sale.getSaleStatus())) {
            throw new IllegalArgumentException("Only PENDING sales can be approved");
        }
        sale.setSaleStatus(SaleStatus.COMPLETED.name().toLowerCase());
        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Override
    @Transactional
    public SaleResponse complete(Long id, String updatedBy) {
        Sale sale = findById(id);
        if (!SaleStatus.PENDING.name().equalsIgnoreCase(sale.getSaleStatus())) {
            throw new IllegalArgumentException("Only PENDING sales can be completed");
        }

        // Deduct stock and log movements
        for (SaleItem item : sale.getItems()) {
            Product product = productService.getProductById(item.getProductId().longValue());
            stockService.decreaseStock(product.getId(), sale.getStoreId().longValue(), item.getQuantity());
            
            transactionService.logStockMovement(
                    TransactionType.SALE,
                    sale.getId(),
                    sale.getHoldRef(),
                    product.getId(),
                    sale.getStoreId().longValue(),
                    null,
                    item.getQuantity(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    SaleStatus.COMPLETED,
                    updatedBy
            );
        }

        sale.setSaleStatus(SaleStatus.COMPLETED.name().toLowerCase());

        Sale grandTotalSale = saleRepository.save(sale);
        return saleMapper.toResponse(grandTotalSale);
    }

    @Override
    @Transactional
    public SaleResponse cancel(Long id, String updatedBy) {
        Sale sale = findById(id);
        if (SaleStatus.CANCELLED.name().equalsIgnoreCase(sale.getSaleStatus()) || 
            SaleStatus.RETURNED.name().equalsIgnoreCase(sale.getSaleStatus())) {
            throw new IllegalArgumentException("Sale already closed or returned");
        }

        if (SaleStatus.COMPLETED.name().equalsIgnoreCase(sale.getSaleStatus())) {
            for (SaleItem item : sale.getItems()) {
                Product product = productService.getProductById(item.getProductId().longValue());
                stockService.increaseStock(
                        product.getId(),
                        sale.getStoreId().longValue(),
                        item.getQuantity(),
                        product.getCostPrice()
                );
                
                transactionService.logStockMovement(
                        TransactionType.ADJUSTMENT_IN,
                        sale.getId(),
                        "HOLD " + sale.getHoldRef(),
                        product.getId(),
                        sale.getStoreId().longValue(),
                        null,
                        item.getQuantity(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        SaleStatus.CANCELLED,
                        updatedBy
                );
            }
        }

        sale.setSaleStatus(SaleStatus.CANCELLED.name().toLowerCase());
        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Override
    @Transactional
    public SaleResponse returnSale(Long id, String updatedBy) {
        Sale sale = findById(id);
        if (SaleStatus.RETURNED.name().equalsIgnoreCase(sale.getSaleStatus())) {
            throw new IllegalArgumentException("Sale already returned");
        }
        if (!SaleStatus.COMPLETED.name().equalsIgnoreCase(sale.getSaleStatus())) {
            throw new IllegalArgumentException("Only COMPLETED sales can be returned");
        }

        for (SaleItem item : sale.getItems()) {
            Product product = productService.getProductById(item.getProductId().longValue());
            stockService.increaseStock(
                    product.getId(),
                    sale.getStoreId().longValue(),
                    item.getQuantity(),
                    product.getCostPrice()
            );

            transactionService.logStockMovement(
                    TransactionType.ADJUSTMENT_IN,
                    sale.getId(),
                    "RET-" + sale.getHoldRef(),
                    product.getId(),
                    sale.getStoreId().longValue(),
                    null,
                    item.getQuantity(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    SaleStatus.RETURNED,
                    updatedBy
            );
        }

        sale.setSaleStatus(SaleStatus.RETURNED.name().toLowerCase());
        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Override
    @Transactional
    public void delete(Long id, String deletedBy) {
        Sale sale = findById(id);
        if (SaleStatus.COMPLETED.name().equalsIgnoreCase(sale.getSaleStatus())) {
            throw new IllegalArgumentException("Cannot delete completed sale");
        }
        
        sale.setDeleteFlag(1);
        saleRepository.save(sale);
    }

    @Override
    public Sale findById(Long id) {
        return saleRepository.findById(id)
                .filter(s -> s.getDeleteFlag() == 0)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", id));
    }

    @Override
    @Transactional
    public SaleResponse updateStatus(Long id, SaleStatus status, String updatedBy) {
        if (status == SaleStatus.COMPLETED) {
            return complete(id, updatedBy);
        }
        if (status == SaleStatus.CANCELLED) {
            return cancel(id, updatedBy);
        }
        if (status == SaleStatus.RETURNED) {
            return returnSale(id, updatedBy);
        }
        Sale sale = findById(id);
        sale.setSaleStatus(SaleStatus.PENDING.name().toLowerCase());
        return saleMapper.toResponse(saleRepository.save(sale));
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
        item.setCost(product.getCostPrice() != null ? product.getCostPrice() : BigDecimal.ZERO);
        BigDecimal disc = itemReq.getItemDiscount() != null ? itemReq.getItemDiscount() : BigDecimal.ZERO;
        item.setItemDiscount(disc);
        BigDecimal subtotal = itemReq.getQuantity().multiply(itemReq.getUnitPrice()).subtract(disc);
        item.setSubtotal(subtotal);
        return item;
    }

    private void calculateSaleTotals(Sale sale) {
        BigDecimal total = sale.getItems().stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        sale.setTotal(total);
        BigDecimal orderDisc = sale.getOrderDiscount() != null ? sale.getOrderDiscount() : BigDecimal.ZERO;
        sale.setGrandTotal(total.subtract(orderDisc));
    }
}