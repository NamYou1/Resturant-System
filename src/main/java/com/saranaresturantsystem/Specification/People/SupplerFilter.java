package com.saranaresturantsystem.Specification.People;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplerFilter {
    private  String name;
    private  String addressOne;
    private  String addressTwo;
    private  String phone ;
    private  String email ;
    private String address ;
    private  String status;
}
