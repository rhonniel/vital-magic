package com.lps.vitalMagic.inventory.domain.repository;

import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionOperation;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;

import java.util.List;
import java.util.Optional;

public interface InventoryTransactionRepository {
    List<InventoryTransaction> findAllUnprocessedTransactions();
    List<InventoryTransaction> findAll();
    Optional<InventoryTransaction> findById(Long id);
    InventoryTransaction save(InventoryTransaction  inventoryTransaction);

}
