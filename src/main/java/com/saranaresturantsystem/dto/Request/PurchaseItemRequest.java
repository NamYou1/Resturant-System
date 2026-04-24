package com.saranaresturantsystem.dto.request;

import lombok.Data;
@Data
public class PurchaseItemRequest {
    private Long productId;
    private Double quantity;
    private Double cost;
    private Double totalDiscount;
}
