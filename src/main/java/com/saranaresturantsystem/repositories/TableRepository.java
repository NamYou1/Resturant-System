package com.saranaresturantsystem.repositories;


import com.saranaresturantsystem.entities.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TableRepository extends JpaRepository<Tables, Long>, JpaSpecificationExecutor<Tables> {
    boolean existsByName(String name);
//    boolean existsByOrderNumber(String orderNumber);
    Optional<Tables> findByName(String name);
}
