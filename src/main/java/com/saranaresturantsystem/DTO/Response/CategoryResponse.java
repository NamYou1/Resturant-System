package com.saranaresturantsystem.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class CategoryResponse {
    private Long id;
    private String code;
    private String name;
    private String display;
    private String imageUrl;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;


}

