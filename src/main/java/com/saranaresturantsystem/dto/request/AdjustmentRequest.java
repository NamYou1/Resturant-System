package com.saranaresturantsystem.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdjustmentRequest {
    private String referenceNo;
    @NotNull(message = "Store ID is required")
    private Integer storeId;
    private String note;
    private String file;
    private List<AdjustmentItemRequest> items;
}
