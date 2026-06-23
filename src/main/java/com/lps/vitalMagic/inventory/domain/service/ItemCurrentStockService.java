package com.lps.vitalMagic.inventory.domain.service;

import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;
import com.lps.vitalMagic.inventory.domain.repository.InventoryTransactionRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemInventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
// Todo cual es la ventaja de que esto sea un bean de spring
public class ItemCurrentStockService {

    private final ItemInventoryRepository itemInventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;


    public ItemCurrentStockService(ItemInventoryRepository itemInventoryRepository, InventoryTransactionRepository inventoryTransactionRepository) {
        this.itemInventoryRepository = itemInventoryRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }

    public Integer getCurrentStock(Long itemId){
       ItemInventory itemInventory= itemInventoryRepository.findByActiveTrueAndItemId(itemId)
               .orElseThrow((EntityNotFoundException::new));

       Integer totalUnprocessed=inventoryTransactionRepository.findTotalUnprocessedStocksByItemId(itemId);

       return itemInventory.getCurrentStock()+totalUnprocessed;
    }
}
