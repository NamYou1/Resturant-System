package com.saranaresturantsystem.repositories.inventory;

import com.saranaresturantsystem.entities.inventory.TransferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferItemRepository extends JpaRepository<TransferItem, Long> {

    List<TransferItem> findByTransferId(Long transferId);

    @Modifying
    @Query("DELETE FROM TransferItem ti WHERE ti.transfer.id = :transferId")
    void deleteByTransferId(@Param("transferId") Long transferId);
}