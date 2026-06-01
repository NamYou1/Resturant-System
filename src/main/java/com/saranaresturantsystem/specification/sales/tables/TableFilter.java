package com.saranaresturantsystem.specification.sales.tables;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableFilter {
    private String name;
    private String orderNumber;
}
