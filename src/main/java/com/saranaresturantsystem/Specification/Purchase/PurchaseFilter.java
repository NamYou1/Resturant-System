package com.saranaresturantsystem.Specification.Purchase;

import lombok.Data;

@Data
public class PurchaseFilter {
    private String reference;
    private Long supplierId;
    private Long storeId;
    private String status;
}