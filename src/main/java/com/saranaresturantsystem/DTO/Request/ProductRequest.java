package com.saranaresturantsystem.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Code must be at most 50 characters")
    private String code;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    private BigDecimal price;

    private BigDecimal cost;

    private String image;

    @Size(max = 20, message = "Type must be at most 20 characters")
    private String type; // standard, combo, service, etc.

    private String details;

    @PositiveOrZero(message = "Alert quantity cannot be negative")
    private BigDecimal alertQuantity;

    private Integer showFlag;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Long sectionId; // Refers to SubCategory

    @NotNull(message = "Base unit is required")
    private Long unitId;

    private Integer defaultSaleUnit;

    private Integer defaultPurchaseUnit;

    private Integer printer;
}