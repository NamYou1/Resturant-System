package com.saranaresturantsystem.dto.request.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    private String name;
    private String phone;
    private String email;
    private String address;
    private Long storeId;
    private Long customerGroupId;
    private Long priceGroupId;
    private Long tableGroupId;
}
