package com.lps.vitalMagic.inventory.domain.repository;

import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InventoryTransactionRepository {
    List<InventoryTransaction> findAllUnprocessedTransactions();
    List<InventoryTransaction> findAll();
    Optional<InventoryTransaction> findById(Long id);
    InventoryTransaction save(InventoryTransaction  inventoryTransaction);

    Integer findTotalUnprocessedStocksByItemId(Long id);

    // Returns the number of still-pending records marked; caller owns the transaction.
    int markAsProcessed(List<Long> transactionIds, LocalDateTime processedAt);
}
