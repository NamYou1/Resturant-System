package com.saranaresturantsystem.specification.purchase;

import lombok.Data;

@Data
public class PurchaseFilter {
    private String reference;
    private Long supplierId;
    private Long storeId;
    private String status;
}