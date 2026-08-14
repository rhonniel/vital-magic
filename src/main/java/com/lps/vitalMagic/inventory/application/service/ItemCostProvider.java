package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.domain.repository.ItemInventoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
//Todo faltan los test de esto
public class ItemCostProvider {
    private final ItemInventoryRepository itemInventoryRepository;

    public ItemCostProvider(ItemInventoryRepository itemInventoryRepository) {
        this.itemInventoryRepository = itemInventoryRepository;
    }

    public Map<Long, BigDecimal> findCostsByItemIds(Set<Long> itemIds) {
        Map<Long, BigDecimal> itemCosts = new HashMap<>();

        if (itemIds.isEmpty()) {
            return itemCosts;
        }

        itemInventoryRepository.findByItemIds(itemIds).forEach(itemInventory -> {
            itemCosts.put(itemInventory.getItemId(), itemInventory.getUnitCost());
        });

        return itemCosts;
    }
}
