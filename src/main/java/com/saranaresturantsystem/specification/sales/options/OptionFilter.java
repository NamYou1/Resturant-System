package com.saranaresturantsystem.specification.sales.options;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionFilter {
    private String name;
    private Long groupId;
    private String groupName;
    private BigDecimal price;
    private Boolean deleteFlag;
}
