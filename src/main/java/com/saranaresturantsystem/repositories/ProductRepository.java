package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByCode(String code);
    boolean existsByCode(String code);
    List<Product> findByCategoryId(Long categoryId);
    @Query("SELECT p FROM Product p JOIN ProductStoreQty q ON q.product.id = p.id " +
            "WHERE q.storeId = :storeId AND q.quantity <= p.alertQuantity")
    List<Product> findLowStockProducts(@Param("storeId") Integer storeId);
}