package com.saranaresturantsystem.specification.users.purchases;

import lombok.Data;

@Data
public class PurchaseFilter {
    private String reference;
    private Long supplierId;
    private Long storeId;
    private String status;
}