package com.saranaresturantsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRequest {

    @NotBlank(message = "Store name is required")
    @Size(max = 50)
    private String name;

    @NotBlank(message = "Store code is required")
    @Size(max = 20)
    private String code;

    @Size(max = 40)
    private String logo;

    @Email(message = "Invalid email format")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(max = 25)
    private String phone;

    @Size(max = 200)
    private String address1;

    @Size(max = 200)
    private String address2;

    @Size(max = 20)
    private String city;

    @Size(max = 20)
    private String state;

    @Size(max = 8)
    private String postalCode;

    @Size(max = 25)
    private String country;

    @Size(max = 3)
    private String currencyCode;

    private String receiptHeader;

    private String receiptFooter;
}