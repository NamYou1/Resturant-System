package com.saranaresturantsystem.dto.Response;

import com.saranaresturantsystem.entities.status.StatusType;
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
