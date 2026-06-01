package com.saranaresturantsystem.dto.request.purchases;

import com.saranaresturantsystem.enums.PaymentStatus;
import com.saranaresturantsystem.enums.PurchaseStatus;
import com.saranaresturantsystem.enums.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseRequest {
    private String reference;
    private LocalDateTime date;
    private String note;
    @NotNull(message = "supplierId is required")
    private Long supplierId;
    @NotNull(message = "sellerId is required")
    private Long sellerId;
    @NotNull(message = "storeId is required")
    private Long storeId;
    private StatusType status = StatusType.ACTIVE;
    private PurchaseStatus purchasesStatus = PurchaseStatus.ORDERED;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private Double orderDiscount;
    private List<PurchaseItemRequest> items;

}