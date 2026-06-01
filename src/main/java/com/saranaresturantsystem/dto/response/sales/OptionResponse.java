package com.saranaresturantsystem.dto.response.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OptionResponse {
    private Long id;
    private String name;
    private Long groupId;
    private String groupName;
    private BigDecimal price;
    private String isActive ;
}
