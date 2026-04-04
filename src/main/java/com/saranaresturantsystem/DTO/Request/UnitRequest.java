package com.saranaresturantsystem.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UnitRequest {

    private Integer baseUnit;

    @NotBlank(message = "Unit code is required")
    @Size(max = 50, message = "Code must be at most 50 characters")
    private String code;

    @NotBlank(message = "Unit name is required")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;

    @Size(max = 5, message = "Operation must be at most 5 characters")
    private String operation; // e.g., "*", "/"

    @NotNull(message = "Operation value is required")
    private BigDecimal operationValue;

    private int showFlag;
}