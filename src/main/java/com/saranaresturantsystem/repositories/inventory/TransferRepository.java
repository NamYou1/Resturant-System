package com.saranaresturantsystem.repositories.inventory;


import com.saranaresturantsystem.entities.inventory.Transfer;
import com.saranaresturantsystem.enums.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long>,
        JpaSpecificationExecutor<Transfer> {


    // Update status only
    @Modifying
    @Query("UPDATE Transfer t SET t.status = :status, t.updatedBy = :updatedBy WHERE t.id = :id")
    void updateStatus(@Param("id") Long id,
                      @Param("status") TransferStatus status,
                      @Param("updatedBy") String updatedBy);
}
