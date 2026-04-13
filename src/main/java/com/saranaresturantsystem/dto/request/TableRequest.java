package com.saranaresturantsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableRequest {
    @NotBlank(message = "Name is required")
    @NotNull(message = "Name can't null")
    @Size(max = 25,message = "Table name must be at most 25 characters")
    private String name;

    @NotBlank(message = "Order Number is required")
    private String orderNumber;
    @NotNull(message = "Group ID is required")
    private Long groupId;
}
