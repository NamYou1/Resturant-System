package com.saranaresturantsystem.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitResponse {
    private Long id;
    private Integer baseUnit;
    private String code;
    private String name;
    private String operation;
    private BigDecimal operationValue;
    private int deleteFlag;
}