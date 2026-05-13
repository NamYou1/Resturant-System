package com.saranaresturantsystem.dto.request;

import com.saranaresturantsystem.entities.status.StatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpensesRequest {
    @NotBlank(message = "Expenses name is required")
    @Size(max =50,message = "Name must be at most 50 characters")
    private String name;

    @NotBlank(message = "Expenses reference is required")
    @Size(max = 50,message = "reference is required")
    private String reference;
    @Positive(message = "Amount price must be greater than 0")
    private BigDecimal amount;

    @Size(max = 50,message = "note is required")
    private String note;

    @Size(max = 50,message = "attachment is required")
    private String attachment;

    @NotNull(message = "Store ID is required")
    private Long storeId;
    @NotNull(message = "Bank ID is required")
    private Long bankId;

    @NotNull(message = "ExpensesType ID required")
    private Long expensesTypeId;

    private StatusType status = StatusType.ACTIVE  ;
    @NotNull(message = "CreateBy required")
    private Integer createBy;

}
