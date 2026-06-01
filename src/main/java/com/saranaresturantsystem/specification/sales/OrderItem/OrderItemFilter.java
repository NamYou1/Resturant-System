package com.saranaresturantsystem.specification.sales.OrderItem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemFilter {
    private Integer userId;
    private Integer orderId;
    private Integer itemId;
    private BigDecimal quantity;
    private LocalDate date;
    private Long tablesId;
}
