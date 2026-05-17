package com.saranaresturantsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OptionResponse {
    private Long id;
    private String name;
    private Long groupId;
    private String groupName;
    private BigDecimal price;
    private Boolean deleteFlag;
}
