package com.saranaresturantsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private  String status ;


}

