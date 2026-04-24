package com.saranaresturantsystem.Repositories;

import com.saranaresturantsystem.Enities.ProductStoreQty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductStoreQtyRepository extends JpaRepository<ProductStoreQty, Long> {
    Optional<ProductStoreQty> findByProductIdAndStoreId(Long productId, Integer storeId);
}
