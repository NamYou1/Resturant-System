package com.saranaresturantsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SellerRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 50)
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull
    private String status;
}