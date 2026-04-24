package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SellerRepository extends JpaRepository<Seller, Long>, JpaSpecificationExecutor<Seller> {
}