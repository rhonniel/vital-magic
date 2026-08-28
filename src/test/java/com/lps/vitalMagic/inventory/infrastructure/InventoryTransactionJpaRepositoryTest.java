package com.lps.vitalMagic.inventory.infrastructure;

import com.lps.vitalMagic.config.MySqlDataJpaTest;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.InventoryTransactionEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.InventoryTransactionJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InventoryTransactionJpaRepositoryTest  extends MySqlDataJpaTest {

    @Autowired
    private InventoryTransactionJpaRepository repository;

    @Autowired
    private ItemJpaRepository itemJpaRepository;

    @Test
    public void calculateTotalUnprocessedStocksSuccessfully() {

        ItemEntity item = itemJpaRepository.save(
                new ItemEntity("Platano", "Platano Fiat dominicano", true)
        );

        Long itemId = item.getId();

        InventoryTransactionEntity purchase = new InventoryTransactionEntity(null,
                itemId, 777L,InventoryTransactionType.PURCHASE,5, BigDecimal.valueOf(777.00),
                null );

        InventoryTransactionEntity sale = new InventoryTransactionEntity(null,
                        itemId, 777L,InventoryTransactionType.SALE,3, BigDecimal.valueOf(777.00),
                        null );

        repository.saveAll(List.of(purchase, sale));
        repository.flush();

        Integer total =
                repository.findTotalUnprocessedStocksByItemId(itemId);

        assertEquals(2,total);
    }
    @Test
    void shouldFindOnlyUnprocessedTransactions() {
        ItemEntity item = itemJpaRepository.saveAndFlush(
                new ItemEntity(
                        "Dragon Tail",
                        "Dragon Tail description",
                        true
                )
        );

        InventoryTransactionEntity pendingPurchase =
                new InventoryTransactionEntity(
                        null,
                        item.getId(),
                        101L,
                        InventoryTransactionType.PURCHASE,
                        5,
                        new BigDecimal("20.00"),
                        null
                );

        InventoryTransactionEntity pendingSale =
                new InventoryTransactionEntity(
                        null,
                        item.getId(),
                        102L,
                        InventoryTransactionType.SALE,
                        3,
                        new BigDecimal("20.00"),
                        null
                );

        InventoryTransactionEntity processedTransaction =
                new InventoryTransactionEntity(
                        null,
                        item.getId(),
                        103L,
                        InventoryTransactionType.PURCHASE,
                        10,
                        new BigDecimal("20.00"),
                        LocalDateTime.now()
                );

        repository.saveAllAndFlush(
                List.of(
                        pendingPurchase,
                        pendingSale,
                        processedTransaction
                )
        );

        List<InventoryTransactionEntity> result =
                repository.findAllUnprocessedTransactions();

        List<Long> resultIds = result.stream()
                .map(InventoryTransactionEntity::getId)
                .toList();

        assertEquals(2, result.size());
        assertTrue(resultIds.contains(pendingPurchase.getId()));
        assertTrue(resultIds.contains(pendingSale.getId()));
        assertFalse(resultIds.contains(processedTransaction.getId()));
    }

}
