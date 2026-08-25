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

}
