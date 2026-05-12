package com.saranaresturantsystem.specification.purchases.ExpensesType;

import com.saranaresturantsystem.entities.status.StatusType;
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
