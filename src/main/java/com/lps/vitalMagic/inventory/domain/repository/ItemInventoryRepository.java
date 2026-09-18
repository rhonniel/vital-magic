package com.lps.vitalMagic.inventory.domain.repository;

import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ItemInventoryRepository {
    List<ItemInventory> findAllActive();
    Optional<ItemInventory> findById(Long Id);
    List<ItemInventory> findByItemIds(Set<Long> itemIds);
    List<ItemInventory> findItemsWithLowStock();
    ItemInventory save(ItemInventory itemInventory);

    Optional<ItemInventory> findByActiveTrueAndItemId(Long itemId);

    // Returns the number of active inventories matched; caller owns the transaction.
    int addToCurrentStock(Long itemId, int quantity);
}
