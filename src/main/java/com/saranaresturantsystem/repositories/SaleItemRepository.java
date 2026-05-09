package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
}