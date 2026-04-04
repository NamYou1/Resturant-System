package com.saranaresturantsystem.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data

public class SupplierRequest {
    @NotNull(message = "Name is required ")
    private  String name;
    private  String addressOne;
    private  String addressTwo;
    @NotNull(message = "Phone is required ")
    @Size(max = 15, message = "Phone number must be at most 15 characters")
    private  String phone ;
    private  String email ;
    private String address ;
    @NotNull(message = "Status must be at most 5 characters")
    @Size(max = 5)
    private  String status;
}
