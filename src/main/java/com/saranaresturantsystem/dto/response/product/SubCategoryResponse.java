package com.saranaresturantsystem.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubCategoryResponse {
    private  Long id ;
    private  String section ;
    private  String status ;
    private Long categoryId;
    private String categoryName ;
}
