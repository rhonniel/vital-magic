package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;
import com.lps.vitalMagic.inventory.domain.repository.InventoryTransactionRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemInventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemCurrentStockService {

    private final ItemInventoryRepository itemInventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;


    public ItemCurrentStockService(ItemInventoryRepository itemInventoryRepository, InventoryTransactionRepository inventoryTransactionRepository) {
        this.itemInventoryRepository = itemInventoryRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }

    public Integer getCurrentStock(Long itemId){
       ItemInventory itemInventory= itemInventoryRepository.findByActiveTrueAndItemId(itemId)
               .orElseThrow(() -> new IllegalStateException( "No active inventory found for item " + itemId
               ));

       Integer totalUnprocessed=inventoryTransactionRepository.findTotalUnprocessedStocksByItemId(itemId);

       return itemInventory.getCurrentStock()+totalUnprocessed;
    }
}
