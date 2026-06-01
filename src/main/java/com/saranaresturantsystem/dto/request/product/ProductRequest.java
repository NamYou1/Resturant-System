package com.saranaresturantsystem.dto.request.product;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    /** Product code, required */
    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Code must be at most 50 characters")
    private String code;
    /** Product name, required */
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;
    @Positive(message = "Cost price must be greater than 0")
    private BigDecimal costPrice;
    @Positive(message = "Sale price must be greater than 0" )
    private BigDecimal salePrice;
//    @Schema(type = "string", format = "binary", nullable = true)
//    private MultipartFile image;
    @Size(max = 20, message = "Type must be at most 20 characters")
    private String type;
    private String details;
    @PositiveOrZero(message = "Alert quantity cannot be negative")
    private BigDecimal alertQuantity;
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    @NotNull(message = "sectionId is required")
    private Long sectionId;
    @NotNull(message = "Base unit is required")
    private Long unitId;
    private Integer defaultSaleUnit;
    private Integer defaultPurchaseUnit;
    private Integer printer;
    private StatusType status = StatusType.ACTIVE;
}