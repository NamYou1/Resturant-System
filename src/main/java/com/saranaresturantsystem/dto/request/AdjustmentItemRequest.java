package com.saranaresturantsystem.dto.request;

import lombok.Data;

@Data
public class AdjustmentItemRequest {
    private Long productId;
    private Long quantity;
}
