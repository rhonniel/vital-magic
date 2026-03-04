package com.lps.vitalMagic.inventory.domain.repository;

import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionConcept;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionOperation;

import java.util.List;
import java.util.Optional;

public interface InventoryTransactionRepository {
    List<InventoryTransaction> findAllUnprocessedTransactions();
    List<InventoryTransaction> findByConcept(InventoryTransactionConcept concept);
    List<InventoryTransaction> findByOperation(InventoryTransactionOperation operation);
    Optional<InventoryTransaction> findById(Long id);
    InventoryTransaction save(InventoryTransaction  inventoryTransaction);

}
