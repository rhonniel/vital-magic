package com.lps.vitalMagic.inventory.infrastructure.persistence.repository;

import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.InventoryTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryTransactionJpaRepository extends JpaRepository<InventoryTransactionEntity,Long> {
    @Query("select i from InventoryTransactionEntity i where i.processAt is null ")
    List<InventoryTransactionEntity> findAllUnprocessedTransactions();
}
