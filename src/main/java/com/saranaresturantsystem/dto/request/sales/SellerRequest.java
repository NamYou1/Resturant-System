package com.saranaresturantsystem.dto.request.sales;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private StatusType status = StatusType.ACTIVE;
}