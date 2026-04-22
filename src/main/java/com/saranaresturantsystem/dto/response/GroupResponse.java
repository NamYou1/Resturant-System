package com.saranaresturantsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {
    private Long id;
    private String name;
    private String description;
    private  String status ;
}
