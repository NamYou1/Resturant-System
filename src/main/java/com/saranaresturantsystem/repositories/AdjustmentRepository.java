package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Adjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdjustmentRepository extends JpaRepository<Adjustment,Long>, JpaSpecificationExecutor<Adjustment> {
}
