package com.saranaresturantsystem.repositories.sales;

import com.saranaresturantsystem.entities.sales.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
}