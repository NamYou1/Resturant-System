package com.saranaresturantsystem.Specification.Unit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitFilter {
    private String name;
    private String code;
    private Integer baseUnit;
    private Integer deleteFlag;
}