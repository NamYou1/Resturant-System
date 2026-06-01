package com.saranaresturantsystem.dto.request.sales;

import com.saranaresturantsystem.enums.StatusType;
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
public class OptionRequest {
    @NotBlank(message = "Option name is required")
    @Size(max=50,message = "Name must be at most 50 characters")
    private String name;
    @NotNull(message = "Group ID is required")
    private Long groupId;
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;
    private StatusType isActive = StatusType.ACTIVE;
}
