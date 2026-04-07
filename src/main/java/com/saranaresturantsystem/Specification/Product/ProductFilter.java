package com.saranaresturantsystem.Specification.Product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductFilter {
    private String name;
    private String code;
    private Long categoryId;
    private Long sectionId; // SubCategory
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer showFlag;
}