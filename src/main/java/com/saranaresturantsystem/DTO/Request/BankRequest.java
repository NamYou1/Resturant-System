package com.saranaresturantsystem.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankRequest {
    @NotBlank(message = "Bank name is required")
    @Size(max = 100,message = "Name must be between 3 and 100 characters")
    private String name;
    @Size(max = 50,message = "Number must be between 50")
    private  String number;
//    @NotNull(message = "amount is reques")
    private String amount;
    @Size(max = 50,message = "is_default must be between")
    private String is_default;
    @Size(max = 520,message = "statement must be between")
    private String statement;
    private LocalDate fromTime;
    private LocalDate toTime;
    @Size(max = 3  , message = "Status must not exceed 3 characters")
    @NotNull
    private  String status ;
}
