package com.saranaresturantsystem.DTO.Response;

import lombok.Data;

@Data
public class SellerResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String status;
}