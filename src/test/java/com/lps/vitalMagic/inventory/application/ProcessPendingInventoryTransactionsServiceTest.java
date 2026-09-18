package com.lps.vitalMagic.inventory.application;

import com.lps.vitalMagic.inventory.application.service.ProcessPendingInventoryTransactionsService;
import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;
import com.lps.vitalMagic.inventory.domain.repository.InventoryTransactionRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemInventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPendingInventoryTransactionsServiceTest {
    @Mock private InventoryTransactionRepository transactions;
    @Mock private ItemInventoryRepository inventories;
    @InjectMocks private ProcessPendingInventoryTransactionsService service;

    @Test
    void shouldAggregateStockAndMarkExactlyTheCapturedTransactions() {
        when(transactions.findAllUnprocessedTransactions()).thenReturn(List.of(
                transaction(1L, 10L, InventoryTransactionType.PURCHASE, 8),
                transaction(2L, 10L, InventoryTransactionType.SALE, 3),
                transaction(3L, 20L, InventoryTransactionType.SALE, 2)));
        when(inventories.addToCurrentStock(10L, 5)).thenReturn(1);
        when(inventories.addToCurrentStock(20L, -2)).thenReturn(1);
        when(transactions.markAsProcessed(eq(List.of(1L, 2L, 3L)), any(LocalDateTime.class))).thenReturn(3);

        service.execute();

        verify(inventories).addToCurrentStock(10L, 5);
        verify(inventories).addToCurrentStock(20L, -2);
        verify(transactions).markAsProcessed(eq(List.of(1L, 2L, 3L)), any(LocalDateTime.class));
    }

    @Test
    void shouldNotWriteWhenNothingIsPending() {
        when(transactions.findAllUnprocessedTransactions()).thenReturn(List.of());

        service.execute();

        verifyNoInteractions(inventories);
        verify(transactions, never()).markAsProcessed(anyList(), any());
    }

    @Test
    void shouldFailWhenActiveInventoryIsMissing() {
        when(transactions.findAllUnprocessedTransactions()).thenReturn(List.of(
                transaction(1L, 10L, InventoryTransactionType.PURCHASE, 8)));
        when(inventories.addToCurrentStock(10L, 8)).thenReturn(0);

        assertThrows(IllegalStateException.class, service::execute);

        verify(transactions, never()).markAsProcessed(anyList(), any());
    }

    @Test
    void shouldFailWhenNotAllCapturedTransactionsAreMarked() {
        when(transactions.findAllUnprocessedTransactions()).thenReturn(List.of(
                transaction(1L, 10L, InventoryTransactionType.PURCHASE, 8)));
        when(inventories.addToCurrentStock(10L, 8)).thenReturn(1);
        when(transactions.markAsProcessed(eq(List.of(1L)), any())).thenReturn(0);

        assertThrows(IllegalStateException.class, service::execute);
    }

    private InventoryTransaction transaction(Long id, Long itemId, InventoryTransactionType type, int quantity) {
        return InventoryTransaction.from(id, itemId, 100L, quantity, type, null, null);
    }
}
