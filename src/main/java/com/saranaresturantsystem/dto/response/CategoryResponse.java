package com.saranaresturantsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String code;
    private String name;
    private String display;
    private String imageUrl;
    private LocalDate fromTime;
    private LocalDate toTime;
    private  String status ;


}

