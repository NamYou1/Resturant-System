package com.saranaresturantsystem.dto.Response;

import com.saranaresturantsystem.entities.status.GeneralStatus;
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
    private GeneralStatus status;
    private BigDecimal total;
    private Long storeId;
    private String note;
    private Integer createBy;
    private String file;
    private List<AdjustmentItemResponse> items;
}
