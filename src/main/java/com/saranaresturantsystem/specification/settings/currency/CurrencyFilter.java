package com.saranaresturantsystem.specification.settings.currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyFilter {
    private  String code ;
    private  String name ;
}
