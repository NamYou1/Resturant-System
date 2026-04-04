package com.saranaresturantsystem.Specification.Category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.saranaresturantsystem.Enities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubCategoryFilter {
//    private  long id ;
    private  String section ;
    private Category categoryId ;
    private String status ;
}
