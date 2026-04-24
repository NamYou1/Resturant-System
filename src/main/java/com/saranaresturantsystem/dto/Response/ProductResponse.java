package com.saranaresturantsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String code;
    private String name;
    private BigDecimal price;
    private BigDecimal cost;
    private String image;
    private String type;
    private String details;
    private BigDecimal alertQuantity;
    private Integer showFlag;
    private Long categoryId;
    private String categoryName;
    private Long sectionId;
    private String sectionName;
    private Long unitId;
    private String unitName;
    private Integer defaultSaleUnit;
    private Integer defaultPurchaseUnit;
    private Integer printer;
}