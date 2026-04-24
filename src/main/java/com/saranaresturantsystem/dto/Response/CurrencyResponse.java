package com.saranaresturantsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyResponse {
    private  Long id ;
    private  String code ;
    private  String name ;
    private  String operation ;
    private  double rate ;
    private  String symbol ;
}
