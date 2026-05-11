package com.saranaresturantsystem.dto.request;

import com.saranaresturantsystem.entities.status.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Code must be at most 50 characters")
    @NotNull(message = "Code is required")
    @NotBlank(message = "Product name is required")
    private String code;
    @Size(max = 255, message = "Name must be at most 255 characters")
    @NotNull(message = "Name is required")
    private String name;
    @Positive(message = "Cost price must be greater than 0")
    private BigDecimal costPrice;
    @Positive(message = "Sale price must be greater than 0" )
    private BigDecimal salePrice;
    @Schema(type = "string", format = "binary", nullable = true)
    private MultipartFile image;
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