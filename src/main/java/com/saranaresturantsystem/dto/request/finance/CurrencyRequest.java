package com.saranaresturantsystem.dto.request.finance;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRequest {
    @NotNull(message = "Currency code is required")
    private  String code ;
    @NotNull(message = "Currency name is required")
    private  String name ;
    private String operation ;
    private  double rate ;
    private  String symbol ;

    private StatusType status = StatusType.ACTIVE;
}
