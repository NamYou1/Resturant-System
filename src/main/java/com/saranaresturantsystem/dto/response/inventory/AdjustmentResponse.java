package com.saranaresturantsystem.dto.response.inventory;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdjustmentResponse {
    private Long id;
    private LocalDateTime date;
    private String referenceNo;
    private String status;
    private BigDecimal total;
    private Long storeId;
    private String note;
    private String createdBy;
    private String file;
    private List<AdjustmentItemResponse> items;
}
