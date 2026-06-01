package com.saranaresturantsystem.dto.request.product;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubCategoryRequest {
    @NotNull(message = "Section is required ")
    private  String section ;
    @NotNull(message = "Category is required ")
    private Long categoryId ;
    private StatusType status = StatusType.ACTIVE;
}
