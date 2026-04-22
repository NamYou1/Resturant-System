package com.saranaresturantsystem.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PurchaseItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productCode;
    private BigDecimal quantity;
    private BigDecimal cost;
    private BigDecimal totalDiscount;
    private BigDecimal subtotal;
    private String unitName;
}