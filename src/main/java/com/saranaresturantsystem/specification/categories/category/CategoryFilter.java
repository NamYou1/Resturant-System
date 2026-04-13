package com.saranaresturantsystem.specification.categories.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryFilter {
    private Long id;
    private String code;
    private String name;
}
