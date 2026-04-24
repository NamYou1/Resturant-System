package com.saranaresturantsystem.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaleItemRequest {
    private Integer productId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal itemDiscount;
}