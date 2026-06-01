package com.saranaresturantsystem.specification.sales.customers;

import lombok.Data;

@Data
public class CustomerFilter {
    private String name;
    private Long storeId;
    private Long customerGroupId;
}
