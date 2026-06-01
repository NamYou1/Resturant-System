package com.saranaresturantsystem.services;

import com.saranaresturantsystem.enums.SaleStatus;
import com.saranaresturantsystem.enums.TransactionType;

import java.math.BigDecimal;

public interface TransactionService {
    void logStockMovement(
            TransactionType type,
            Long referenceId,
            String referenceNo,
            Long productId,
            Long storeId,
            Long unitId,
            BigDecimal qty,
            BigDecimal unitQty,
            BigDecimal price,
            SaleStatus status,
            String createdBy
    );
}
