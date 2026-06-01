package com.saranaresturantsystem.dto.request.purchases;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItemRequest {
    @NotNull(message = "productId is required")
    private Long productId;
    @NotNull(message = "quantity is required")
    private Double quantity;
    private Double costPrice;
    private Double totalDiscount;
}
