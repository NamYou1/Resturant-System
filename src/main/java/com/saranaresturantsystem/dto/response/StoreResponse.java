package com.saranaresturantsystem.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {

    private Long id;
    private String name;
    private String code;
    private String logo;
    private String email;
    private String phone;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String currencyCode;
    private String receiptHeader;
    private String receiptFooter;
    private  String status ;
}