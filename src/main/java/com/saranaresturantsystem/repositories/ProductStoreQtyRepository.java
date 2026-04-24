package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.ProductStoreQty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductStoreQtyRepository extends JpaRepository<ProductStoreQty, Long> {
    Optional<ProductStoreQty> findByProductIdAndStoreId(Long productId, Integer storeId);
    @Modifying
    @Query("UPDATE ProductStoreQty p SET p.quantity = p.quantity - :qty " +
            "WHERE p.product.id = :prodId AND p.storeId = :storeId AND p.quantity >= :qty")
    int decreaseStock(Long prodId, Integer storeId, BigDecimal qty);
}