package com.saranaresturantsystem.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
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