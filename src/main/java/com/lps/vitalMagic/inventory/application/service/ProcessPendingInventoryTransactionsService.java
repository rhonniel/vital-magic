package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.application.usecase.ProcessPendingInventoryTransactionsUseCase;
import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.domain.repository.InventoryTransactionRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemInventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ProcessPendingInventoryTransactionsService implements ProcessPendingInventoryTransactionsUseCase {
    private final InventoryTransactionRepository transactions;
    private final ItemInventoryRepository inventories;

    public ProcessPendingInventoryTransactionsService(InventoryTransactionRepository transactions,
                                                      ItemInventoryRepository inventories) {
        this.transactions = transactions;
        this.inventories = inventories;
    }

    @Override
    @Transactional
    public void execute() {
        List<InventoryTransaction> pending = transactions.findAllUnprocessedTransactions();
        if (pending.isEmpty()) {
            return;
        }

        // Use the same captured set for both stock deltas and processing markers.
        Map<Long, Integer> deltas = new HashMap<>();
        for (InventoryTransaction transaction : pending) {
            int delta = transaction.getType().isInbound()
                    ? transaction.getQuantity() : Math.negateExact(transaction.getQuantity());
            deltas.merge(transaction.getItemId(), delta, Math::addExact);
        }

        for (Map.Entry<Long, Integer> delta : deltas.entrySet()) {
            if (inventories.addToCurrentStock(delta.getKey(), delta.getValue()) != 1) {
                throw new IllegalStateException("Expected one active inventory for item " + delta.getKey());
            }
        }

        List<Long> ids = pending.stream().map(InventoryTransaction::getId).toList();
        if (transactions.markAsProcessed(ids, LocalDateTime.now()) != ids.size()) {
            throw new IllegalStateException("Pending inventory transactions changed during reconciliation");
        }
    }
}
