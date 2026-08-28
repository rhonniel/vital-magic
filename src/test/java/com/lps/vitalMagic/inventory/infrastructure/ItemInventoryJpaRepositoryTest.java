package com.lps.vitalMagic.inventory.infrastructure;

import com.lps.vitalMagic.config.MySqlDataJpaTest;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemInventoryJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ItemInventoryJpaRepositoryTest extends MySqlDataJpaTest {

    @Autowired
    private ItemJpaRepository itemRepository;

    @Autowired
    private ItemInventoryJpaRepository inventoryRepository;

    @Test
    void shouldFindOnlyActiveInventories() {
        ItemEntity activeItem = saveItem("Dragon Tail");
        ItemEntity inactiveItem = saveItem("Phoenix Feather");

        saveInventory(activeItem.getId(), 10, 5, true);
        saveInventory(inactiveItem.getId(), 20, 5, false);

        List<ItemInventoryEntity> result =
                inventoryRepository.findByActiveTrue();

        assertEquals(1, result.size());
        assertEquals(
                activeItem.getId(),
                result.get(0).getItemId()
        );
    }

    @Test
    void shouldFindItemsWithStockEqualOrBelowMinimum() {
        ItemEntity belowMinimum = saveItem("Dragon Scale");
        ItemEntity equalToMinimum = saveItem("Troll Tooth");
        ItemEntity aboveMinimum = saveItem("Fairy Dust");

        saveInventory(belowMinimum.getId(), 3, 5, true);
        saveInventory(equalToMinimum.getId(), 5, 5, true);
        saveInventory(aboveMinimum.getId(), 10, 5, true);

        List<ItemInventoryEntity> result =
                inventoryRepository.findItemsWithLowStock();

        List<Long> resultIds = result.stream()
                .map(ItemInventoryEntity::getItemId)
                .toList();

        assertEquals(2, result.size());
        assertTrue(resultIds.contains(belowMinimum.getId()));
        assertTrue(resultIds.contains(equalToMinimum.getId()));
        assertFalse(resultIds.contains(aboveMinimum.getId()));
    }

    @Test
    void shouldFindActiveInventoryByItemId() {
        ItemEntity item = saveItem("Goblin Ear");

        saveInventory(item.getId(), 10, 5, true);

        ItemInventoryEntity result = inventoryRepository
                .findByActiveTrueAndItemId(item.getId())
                .orElseThrow();

        assertEquals(item.getId(), result.getItemId());
        assertTrue(result.isActive());
        assertEquals(10, result.getCurrentStock());
    }

    @Test
    void shouldNotFindInactiveInventoryByItemId() {
        ItemEntity item = saveItem("Ghost Essence");

        saveInventory(item.getId(), 10, 5, false);

        boolean inventoryExists = inventoryRepository
                .findByActiveTrueAndItemId(item.getId())
                .isPresent();

        assertFalse(inventoryExists);
    }

    @Test
    void shouldFindInventoriesByItemIds() {
        ItemEntity firstItem = saveItem("Dragon Tail");
        ItemEntity secondItem = saveItem("Phoenix Feather");
        ItemEntity excludedItem = saveItem("Fairy Dust");

        saveInventory(firstItem.getId(), 10, 5, true);
        saveInventory(secondItem.getId(), 20, 5, true);
        saveInventory(excludedItem.getId(), 30, 5, true);

        List<ItemInventoryEntity> result =
                inventoryRepository.findByItemIdIn(
                        List.of(
                                firstItem.getId(),
                                secondItem.getId()
                        )
                );

        List<Long> resultIds = result.stream()
                .map(ItemInventoryEntity::getItemId)
                .toList();

        assertEquals(2, result.size());
        assertTrue(resultIds.contains(firstItem.getId()));
        assertTrue(resultIds.contains(secondItem.getId()));
        assertFalse(resultIds.contains(excludedItem.getId()));
    }

    private ItemEntity saveItem(String name) {
        return itemRepository.saveAndFlush(
                new ItemEntity(
                        name,
                        name + " description",
                        true
                )
        );
    }

    private void saveInventory(
            Long itemId,
            int currentStock,
            int minStock,
            boolean active
    ) {
        inventoryRepository.saveAndFlush(
                new ItemInventoryEntity(
                        null,
                        itemId,
                        new BigDecimal("25.00"),
                        minStock,
                        currentStock,
                        active
                )
        );
    }
}