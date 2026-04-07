package com.saranaresturantsystem.DTO.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableResponse {
    private Long id;
    private String name;
    private String orderNumber;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private GroupResponse group;
}
