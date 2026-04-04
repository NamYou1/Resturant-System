package com.saranaresturantsystem.Specification.Category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryFilter {
    private Long id;
    private String code;
    private String name;
}
