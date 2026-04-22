package com.saranaresturantsystem.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SupplierResponse {
    private  Long id ;
    private  String name;
    private  String addressOne;
    private  String addressTwo;
    private  String phone ;
    private  String email ;
    private String address ;
    private  String status;
}
