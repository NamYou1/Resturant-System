package com.saranaresturantsystem.dto.Request;

import lombok.Data;

@Data
public class AdjustmentItemRequest {
    private Long productId;
    private Long quantity;
}
