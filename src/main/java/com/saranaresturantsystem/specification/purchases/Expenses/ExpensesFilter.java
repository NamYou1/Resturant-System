package com.saranaresturantsystem.specification.purchases.Expenses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpensesFilter {
    private String name;
    private String reference;
    private LocalDate date;
    private BigDecimal amount;
    private String note;
    private String attachment;
    private Long storeid;
    private Long bankid;
    private Long expensestypeid;
    private BigDecimal createBy;


}
