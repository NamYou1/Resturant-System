package com.saranaresturantsystem.dto.response.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderItemResponse {
    private Integer orderId;
    private Integer itemId;
    private Long id;
    private BigDecimal quantity;
    private LocalDate  date;
    private Long tableId;
    private String tableName;

}
