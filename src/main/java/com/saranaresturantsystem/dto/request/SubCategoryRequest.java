package com.saranaresturantsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubCategoryRequest {
    @NotNull(message = "Section is required ")
    private  String section ;
    @NotNull(message = "Category is required ")
    private Long categoryId ;
}
