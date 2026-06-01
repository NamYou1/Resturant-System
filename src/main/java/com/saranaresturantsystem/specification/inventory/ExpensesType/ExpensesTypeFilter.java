package com.saranaresturantsystem.specification.inventory.ExpensesType;

import com.saranaresturantsystem.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpensesTypeFilter {
    private String name;
    private String description;
    private StatusType status;
}
