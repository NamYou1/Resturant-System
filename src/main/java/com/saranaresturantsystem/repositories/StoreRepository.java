package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Store;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface StoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store> {
}
