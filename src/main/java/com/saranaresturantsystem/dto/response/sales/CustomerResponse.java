package com.saranaresturantsystem.dto.response.sales;

import com.saranaresturantsystem.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String storeName;
    private String tableGroupName;
    private String priceGroupName;
    private String customerGroupId;
    private StatusType status;
}
