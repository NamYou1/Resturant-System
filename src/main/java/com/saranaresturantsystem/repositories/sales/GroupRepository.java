package com.saranaresturantsystem.repositories.sales;

import com.saranaresturantsystem.entities.sales.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
//    boolean existsByName(String name);
//    Optional<Group> findByName(String name);
}
