package com.saranaresturantsystem.DTO.Request;

import lombok.Data;
import java.util.List;

@Data
public class PurchaseRequest {
    private String reference;
    private String note;
    private Long supplierId;
    private Long sellerId;
    private Long storeId;
    private Double orderDiscount;
    private List<PurchaseItemRequest> items;

}