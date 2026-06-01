package com.saranaresturantsystem.dto.request.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleRequest {
    private Long sellerId;
    private Integer customerId;
    private String customerName;
    private Integer storeId;
    private BigDecimal orderDiscount;
    private String holdRef;
    private List<SaleItemRequest> items;
}
