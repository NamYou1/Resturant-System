package com.saranaresturantsystem.specification.adjustments;

import lombok.Data;

@Data
public class AdjustmentFilter {
    private String reference;
    private Long storeId;
    private String status;
}
