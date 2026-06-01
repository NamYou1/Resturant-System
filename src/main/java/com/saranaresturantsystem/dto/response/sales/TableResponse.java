package com.saranaresturantsystem.dto.response.sales;

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
    private  String status ;
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Long  groupId;
    private  String groupName;
}
