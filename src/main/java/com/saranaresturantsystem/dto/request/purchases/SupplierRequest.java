package com.saranaresturantsystem.dto.request.purchases;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.validation.constraints.Email;
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
    @Email(message = "Invalid email format")
    private  String email ;
    private String address ;
    private StatusType status = StatusType.ACTIVE;
}
