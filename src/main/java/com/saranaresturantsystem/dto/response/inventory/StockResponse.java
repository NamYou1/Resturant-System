package com.saranaresturantsystem.dto.response.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {
    private Long id;
    private BigDecimal quantity;
    private BigDecimal costPrice;
    private String productName;
    private String storeName;
}
