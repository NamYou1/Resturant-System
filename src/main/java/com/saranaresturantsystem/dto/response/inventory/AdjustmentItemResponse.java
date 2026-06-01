package com.saranaresturantsystem.dto.response.inventory;

import lombok.Data;

@Data
public class AdjustmentItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productCode;
    private Long quantity;
    private Double cost;
    private Double subTotal;
    private String unit;
}
