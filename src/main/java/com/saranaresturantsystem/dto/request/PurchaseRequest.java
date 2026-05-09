package com.saranaresturantsystem.dto.request;

import com.saranaresturantsystem.entities.status.PaymentStatus;
import com.saranaresturantsystem.entities.status.PurchaseStatus;
import com.saranaresturantsystem.entities.status.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class PurchaseRequest {
    private String reference;
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