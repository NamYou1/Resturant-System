package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
//    boolean existsByName(String name);
//    Optional<Group> findByName(String name);
}
