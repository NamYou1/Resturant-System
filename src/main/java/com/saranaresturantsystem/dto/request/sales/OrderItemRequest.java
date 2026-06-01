package com.saranaresturantsystem.dto.request.sales;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    @NotNull
    private Integer userId;

    @NotNull
    private Integer itemId;

    @NotNull
    @Positive(message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    @NotNull(message = "Date must not be null")
    private LocalDate date;

    @NotNull(message = "Table ID is required")
    private Long tableId;
}
