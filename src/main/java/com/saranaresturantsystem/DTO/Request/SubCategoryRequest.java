package com.saranaresturantsystem.DTO.Request;

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
    private  int showFlag ;
    @NotNull(message = "Status is required ")
    @Size(max = 5, message = "Status must be at most 5 characters")
    private  String status ;
    @NotNull(message = "Category is required ")
    private Long categoryId ;
}
