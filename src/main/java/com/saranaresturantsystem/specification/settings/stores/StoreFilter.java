package com.saranaresturantsystem.specification.settings.stores;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreFilter {
    private  String name;
    private  String code ;
    private  String email ;
    private  String phone ;
    private  String city ;
    private String state ;
    private  String country ;
}
