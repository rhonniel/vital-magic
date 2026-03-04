package com.lps.vitalMagic.inventory.domain.repository;

import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;

import java.util.List;
import java.util.Optional;

public interface ItemInventoryRepository {
    List<ItemInventory> findAllActive();
    Optional<ItemInventory> findById(Long Id);
    List<ItemInventory> findAllItemInventoryWithLowStock();
    ItemInventory save(ItemInventory itemInventory);
}
