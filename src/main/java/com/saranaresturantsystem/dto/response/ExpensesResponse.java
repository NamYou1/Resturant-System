package com.saranaresturantsystem.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpensesResponse {
    private Long id;
    private String name;
    private String reference;
    private LocalDate date;
    private BigDecimal amount;
    private String note;
    private String attachment;

    private Long storeId;
    private String storeName;
    private Long bankId;
    private String bankName;
    private Long expensesTypeId;
    private String expensesTypeName;
    private String status;
    private Integer createBy;
}
