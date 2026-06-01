package com.saranaresturantsystem.specification.sales.sale;

import lombok.Data;

@Data
public class SaleFilter {
    private String customerName;
    private Integer storeId;
    private String status;
}