package com.saranaresturantsystem.dto.request.inventory;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentRequest {
    private String referenceNo;
    @NotNull(message = "Store ID is required")
    private Long storeId;
    private String note;
    private String file;
    private StatusType status = StatusType.ACTIVE;
    private List<AdjustmentItemRequest> items;
}
;