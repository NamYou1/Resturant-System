package com.saranaresturantsystem.dto.request.inventory;

import lombok.Data;

@Data
public class AdjustmentItemRequest {
    private Long productId;
    private Long quantity;
}
