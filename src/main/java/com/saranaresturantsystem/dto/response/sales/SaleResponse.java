package com.saranaresturantsystem.dto.response.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponse {
    private Long id;
    private String referenceNo;
    private String holdRef;
    private LocalDateTime date;
    private String customerName;
    private BigDecimal total;
    private BigDecimal totalDiscount;
    private BigDecimal grandTotal;
    private BigDecimal paid;
    private String status;
    private String saleStatus;
    private List<SaleItemResponse> items;
}
