package com.saranaresturantsystem.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleRequest {
    private Long sellerId;
    private Integer customerId;
    private String customerName;
    private Integer storeId;
    private BigDecimal orderDiscount;
    private String holdRef;
    private List<com.saranaresturantsystem.dto.request.SaleItemRequest> items;
}