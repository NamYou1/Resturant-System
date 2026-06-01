package com.saranaresturantsystem.dto.response.finance;

import com.saranaresturantsystem.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ExpensesTypeResponse {
    private Long id;
    private String name;
    private String description;
    private StatusType status;
}
