package com.saranaresturantsystem.Repositories;

import com.saranaresturantsystem.Enities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SupplierRepository extends JpaRepository<Supplier , Long> , JpaSpecificationExecutor<Supplier> {
}
