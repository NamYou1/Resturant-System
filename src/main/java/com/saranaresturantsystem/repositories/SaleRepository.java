package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {

    @Query("SELECT MAX(s.no) FROM Sale s")
    Integer findMaxNo();



    @Query("SELECT s FROM Sale s WHERE s.deleteFlag = 0 " +
            "AND s.date BETWEEN :startDate AND :endDate " +
            "AND (:storeId IS NULL OR s.storeId = :storeId)")
    List<Sale> getSalesReport(@Param("startDate") LocalDateTime startDate,
                              @Param("endDate") LocalDateTime endDate,
                              @Param("storeId") Integer storeId);
}