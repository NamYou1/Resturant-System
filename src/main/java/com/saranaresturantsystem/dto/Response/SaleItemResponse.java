package com.saranaresturantsystem.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaleItemResponse {
    private Long id;
    private Integer productId;
    private String productName;
    private String productCode;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}