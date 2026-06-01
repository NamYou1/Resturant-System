package com.saranaresturantsystem.dto.request.finance;
import com.saranaresturantsystem.enums.StatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExpensesTypeRequest {
    @NotBlank(message = "Expenses type name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Size(max = 50, message = "Description must be maximum 50 characters")
    private String description;

    private StatusType status = StatusType.ACTIVE;

}
