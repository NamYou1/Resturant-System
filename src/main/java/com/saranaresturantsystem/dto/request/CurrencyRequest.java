package com.saranaresturantsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyRequest {
    @NotNull(message = "Currency code is required")
    private  String code ;
    @NotNull(message = "Currency name is required")
    private  String name ;
    private String operation ;
    private  double rate ;
    private  String symbol ;
}
