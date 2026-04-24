package com.saranaresturantsystem.DTO.Request;

import lombok.Data;
@Data
public class PurchaseItemRequest {
    private Long productId;
    private Double quantity;
    private Double cost;
    private Double totalDiscount;
}
