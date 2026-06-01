package com.saranaresturantsystem.repositories.inventory;

import com.saranaresturantsystem.entities.inventory.ProductStoreQty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductStoreQtyRepository extends JpaRepository<ProductStoreQty, Long> {
    Optional<ProductStoreQty> findByProductIdAndStoreId(Long productId, long storeId);
}
