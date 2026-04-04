package com.saranaresturantsystem.DTO.Response;

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
    private  int showFlag ;
    private  String status ;
    private Long categoryId;
}
