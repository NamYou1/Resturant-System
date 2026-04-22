package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>, JpaSpecificationExecutor<Purchase> {
}
