package com.lps.vitalMagic.inventory.infrastructure;

import com.lps.vitalMagic.config.MySqlIntegrationTest;
import com.lps.vitalMagic.inventory.application.service.ProcessPendingInventoryTransactionsService;
import com.lps.vitalMagic.inventory.application.usecase.ProcessPendingInventoryTransactionsUseCase;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.InventoryTransactionEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.*;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl.JpaInventoryTransactionRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl.JpaItemInventoryRepository;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseEntity;
import com.lps.vitalMagic.purchase.infrastructure.persistance.repository.PurchaseJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=validate")
@Import({ProcessPendingInventoryTransactionsService.class, JpaInventoryTransactionRepository.class,
        JpaItemInventoryRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class ProcessPendingInventoryTransactionsIntegrationTest extends MySqlIntegrationTest {
    @Autowired private ProcessPendingInventoryTransactionsUseCase service;
    @Autowired private ItemJpaRepository items;
    @Autowired private ItemInventoryJpaRepository inventories;
    @Autowired private InventoryTransactionJpaRepository transactions;
    @Autowired private PurchaseJpaRepository purchases;
    @Autowired private JdbcTemplate jdbc;
    @MockitoSpyBean private JpaInventoryTransactionRepository transactionAdapter;

    private final List<Long> itemIds = new ArrayList<>();
    private final List<Long> purchaseIds = new ArrayList<>();

    @AfterEach
    void cleanCommittedFixtures() {
        for (Long itemId : itemIds) {
            jdbc.update("DELETE FROM inventory_transaction WHERE item_id = ?", itemId);
            jdbc.update("DELETE FROM item_inventory WHERE item_id = ?", itemId);
            items.deleteById(itemId);
        }
        purchases.deleteAllById(purchaseIds);
    }

    @Test
    void shouldAggregateMultipleItemsIgnoreProcessedTransactionsAndBeIdempotent() {
        Long first = saveInventory(10);
        Long second = saveInventory(20);
        Long unrelated = saveInventory(30);
        var purchase = saveTransaction(first, InventoryTransactionType.PURCHASE, 8, null, 100L);
        var sale = saveTransaction(first, InventoryTransactionType.SALE, 3, null, 101L);
        var otherSale = saveTransaction(second, InventoryTransactionType.SALE, 4, null, 102L);
        LocalDateTime alreadyProcessedAt = LocalDateTime.of(2026, 9, 15, 12, 0);
        var processed = saveTransaction(first, InventoryTransactionType.PURCHASE, 100, alreadyProcessedAt, 103L);

        service.execute();

        assertEquals(15, stock(first));
        assertEquals(16, stock(second));
        assertEquals(30, stock(unrelated));
        for (var transaction : List.of(purchase, sale, otherSale)) {
            assertNotNull(transactions.findById(transaction.getId()).orElseThrow().getProcessAt());
        }
        LocalDateTime firstProcessedAt = transactions.findById(purchase.getId()).orElseThrow().getProcessAt();
        assertEquals(alreadyProcessedAt, transactions.findById(processed.getId()).orElseThrow().getProcessAt());

        service.execute();

        assertEquals(15, stock(first));
        assertEquals(16, stock(second));
        assertEquals(firstProcessedAt, transactions.findById(purchase.getId()).orElseThrow().getProcessAt());
    }

    @Test
    void shouldProcessPendingPurchasesFromDifferentSourceDates() {
        Long itemId = saveInventory(0);
        // Inventory transactions have no created_at; dates belong to their source purchases.
        for (int day : List.of(16, 17)) {
            var source = purchases.saveAndFlush(new PurchaseEntity(null, new BigDecimal("10.00"),
                    LocalDateTime.of(2026, 9, day, 10, 0)));
            purchaseIds.add(source.getId());
            saveTransaction(itemId, InventoryTransactionType.PURCHASE, 5, null, source.getId());
        }

        service.execute();

        assertEquals(10, stock(itemId));
        assertTrue(transactions.findAllUnprocessedTransactions().isEmpty());
    }

    @Test
    void shouldLeaveInventoryAndProcessedRecordsUnchangedWhenNothingIsPending() {
        Long itemId = saveInventory(10);
        LocalDateTime processedAt = LocalDateTime.of(2026, 9, 16, 12, 0);
        var processed = saveTransaction(itemId, InventoryTransactionType.SALE, 4, processedAt, 100L);

        service.execute();

        assertEquals(10, stock(itemId));
        assertEquals(processedAt, transactions.findById(processed.getId()).orElseThrow().getProcessAt());
    }

    @Test
    void shouldRollBackStockAndMarkersWhenMarkingFailsThenProcessTheBacklogOnRetry() {
        Long itemId = saveInventory(10);
        var pending = saveTransaction(itemId, InventoryTransactionType.PURCHASE, 5, null, 100L);
        var failure = new IllegalStateException("Simulated processing marker failure");
        doAnswer(invocation -> {
            // Prove SQL already changed stock inside the real service transaction.
            assertEquals(15, stock(itemId));
            invocation.callRealMethod();
            throw failure;
        }).doCallRealMethod().when(transactionAdapter).markAsProcessed(anyList(), any());

        assertSame(failure, assertThrows(IllegalStateException.class, service::execute));

        // There is no test transaction: these reads observe the actual rolled-back database.
        assertEquals(10, stock(itemId));
        assertNull(transactions.findById(pending.getId()).orElseThrow().getProcessAt());

        saveTransaction(itemId, InventoryTransactionType.PURCHASE, 2, null, 101L);
        service.execute();
        assertEquals(17, stock(itemId));
        assertTrue(transactions.findAllUnprocessedTransactions().isEmpty());
    }

    @Test
    void shouldRollBackWhenTheProcessedCountDoesNotMatchTheCapturedSet() {
        Long itemId = saveInventory(10);
        var pending = saveTransaction(itemId, InventoryTransactionType.PURCHASE, 5, null, 100L);
        doAnswer(invocation -> {
            invocation.callRealMethod();
            return 0;
        }).when(transactionAdapter).markAsProcessed(anyList(), any());

        assertThrows(IllegalStateException.class, service::execute);

        assertEquals(10, stock(itemId));
        assertNull(transactions.findById(pending.getId()).orElseThrow().getProcessAt());
    }

    @Test
    void shouldMarkOnlyCapturedIdsLeavingNewTransactionsPending() {
        Long itemId = saveInventory(10);
        var captured = saveTransaction(itemId, InventoryTransactionType.PURCHASE, 5, null, 100L);
        List<Long> lateIds = new ArrayList<>();
        doAnswer(invocation -> {
            lateIds.add(saveTransaction(itemId, InventoryTransactionType.PURCHASE, 2, null, 101L).getId());
            return invocation.callRealMethod();
        }).when(transactionAdapter).markAsProcessed(anyList(), any());

        service.execute();

        assertEquals(15, stock(itemId));
        assertNotNull(transactions.findById(captured.getId()).orElseThrow().getProcessAt());
        assertNull(transactions.findById(lateIds.get(0)).orElseThrow().getProcessAt());
    }

    @Test
    void shouldConsumeTransactionsWithZeroNetEffectWithoutChangingStock() {
        Long itemId = saveInventory(10);
        saveTransaction(itemId, InventoryTransactionType.PURCHASE, 5, null, 100L);
        saveTransaction(itemId, InventoryTransactionType.SALE, 5, null, 101L);

        service.execute();

        assertEquals(10, stock(itemId));
        assertTrue(transactions.findAllUnprocessedTransactions().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2})
    void shouldRollBackAllItemsWhenAnotherItemHasNoUniqueActiveInventory(int activeCount) {
        Long first = saveInventory(10);
        Long invalid = saveInventory(20);
        jdbc.update("UPDATE item_inventory SET active = false WHERE item_id = ?", invalid);
        for (int i = 0; i < activeCount; i++) {
            inventories.saveAndFlush(new ItemInventoryEntity(null, invalid, new BigDecimal("2.00"), 0, 20, true));
        }
        var firstPending = saveTransaction(first, InventoryTransactionType.PURCHASE, 5, null, 100L);
        var invalidPending = saveTransaction(invalid, InventoryTransactionType.PURCHASE, 3, null, 101L);

        assertThrows(IllegalStateException.class, service::execute);

        assertEquals(10, stock(first));
        assertTrue(inventories.findByItemIdIn(List.of(invalid)).stream()
                .allMatch(inventory -> inventory.getCurrentStock() == 20));
        assertNull(transactions.findById(firstPending.getId()).orElseThrow().getProcessAt());
        assertNull(transactions.findById(invalidPending.getId()).orElseThrow().getProcessAt());
    }

    private Long saveInventory(int stock) {
        Long itemId = items.saveAndFlush(new ItemEntity("Ingredient", "Description", true)).getId();
        itemIds.add(itemId);
        inventories.saveAndFlush(new ItemInventoryEntity(null, itemId, new BigDecimal("2.00"), 0, stock, true));
        return itemId;
    }

    private InventoryTransactionEntity saveTransaction(Long itemId, InventoryTransactionType type,
                                                       int quantity, LocalDateTime processedAt, Long sourceId) {
        return transactions.saveAndFlush(new InventoryTransactionEntity(null, itemId, sourceId, type,
                quantity, type.isInbound() ? new BigDecimal("2.00") : null, processedAt));
    }

    private int stock(Long itemId) {
        return jdbc.queryForObject("SELECT current_stock FROM item_inventory WHERE item_id = ?",
                Integer.class, itemId);
    }
}
