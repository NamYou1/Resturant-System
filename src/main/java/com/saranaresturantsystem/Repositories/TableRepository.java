package com.saranaresturantsystem.Repositories;


import com.saranaresturantsystem.Enities.TableEnities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TableRepository extends JpaRepository<TableEnities, Long>, JpaSpecificationExecutor<TableEnities> {
    boolean existsByName(String name);
//    boolean existsByOrderNumber(String orderNumber);
    Optional<TableEnities> findByName(String name);
}
