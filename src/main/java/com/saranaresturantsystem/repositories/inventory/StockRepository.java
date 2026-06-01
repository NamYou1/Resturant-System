package com.saranaresturantsystem.repositories.inventory;

import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.entities.inventory.ProductStoreQty;
import com.saranaresturantsystem.entities.inventory.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<ProductStoreQty, Long> {
    Optional<ProductStoreQty> findByProductAndStore(Product product, Store store);
    Page<ProductStoreQty> findByStore(Long storeId, Pageable pageable);
}
