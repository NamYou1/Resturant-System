package com.saranaresturantsystem.DTO.Response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseResponse {
    private Long id;
    private String reference;
    private LocalDateTime date;
    private String supplierName;
    private String storeName;
    private String sellerName;
    private BigDecimal total;
    private BigDecimal totalDiscount;
    private BigDecimal grandTotal;
    private String purchasesStatus;
    private List<PurchaseItemResponse> items;
}