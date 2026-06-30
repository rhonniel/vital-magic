package com.lps.vitalMagic.inventory.domain.repository;

import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryTransactionRepository {
    List<InventoryTransaction> findAllUnprocessedTransactions();
    List<InventoryTransaction> findAll();
    Optional<InventoryTransaction> findById(Long id);
    InventoryTransaction save(InventoryTransaction  inventoryTransaction);

    Integer findTotalUnprocessedStocksByItemId(Long id);
}
