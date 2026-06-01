package com.saranaresturantsystem.services.impl.finance;

import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.entities.inventory.Store;
import com.saranaresturantsystem.entities.finance.Transaction;
import com.saranaresturantsystem.entities.product.Unit;
import com.saranaresturantsystem.enums.SaleStatus;
import com.saranaresturantsystem.enums.TransactionType;
import com.saranaresturantsystem.execption.ApiException;
import com.saranaresturantsystem.repositories.inventory.TransactionRepository;
import com.saranaresturantsystem.services.TransactionService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j

public class TransactionServiceImp implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final EntityManager entityManager;
    @Override
    @Transactional
    public void logStockMovement(
            TransactionType type, Long referenceId, String referenceNo,
            Long productId, Long storeId, Long unitId,
            BigDecimal qty, BigDecimal unitQty, BigDecimal price,
            SaleStatus status, String createdBy) {

        try {
            Transaction tx = new Transaction();
            tx.setTranDate(LocalDateTime.now());
            tx.setTransactionType(type);
            tx.setReferenceId(referenceId);
            tx.setReferenceNo(referenceNo);
            tx.setUnitQuantity(unitQty);
            tx.setPricePerUnit(price);
            tx.setStatus(status);
            tx.setCreatedBy(createdBy);

            if (type == TransactionType.SALE || type == TransactionType.TRANSFER_OUT || type == TransactionType.ADJUSTMENT_OUT) {
                tx.setQuantity(qty.negate());
            } else {
                tx.setQuantity(qty);
            }

            tx.setTotalAmount(qty.abs().multiply(price));
            if (productId != null) {
                tx.setTblProduct(entityManager.getReference(Product.class, productId));
            }
            if (storeId != null) {
                tx.setTblStore(entityManager.getReference(Store.class, storeId));
            }
            if (unitId != null) {
                tx.setTblUnit(entityManager.getReference(Unit.class, unitId));
            }

            transactionRepository.save(tx);
            log.info("Stock transaction logged successfully for Ref: {}, Type: {}", referenceNo, type);

        } catch (Exception e) {
            log.error("Failed to log stock transaction for Ref: {}. Error: {}", referenceNo, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to record inventory transaction: " + e.getMessage());
        }
    }
}
