package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.domain.repository.InventoryTransactionRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RegisterPurchaseTransactionService {
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final ItemRepository itemRepository;

    public RegisterPurchaseTransactionService(InventoryTransactionRepository inventoryTransactionRepository, ItemRepository itemRepository) {
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.itemRepository = itemRepository;
    }

    //TODO the list parameter look pretty long, i work with this when start define the ACL
    public void registerPurchase(Long purchaseId, Long itemId, int quantity, BigDecimal unitCost){
        if(itemRepository.existsById(itemId)) {
            inventoryTransactionRepository.save(InventoryTransaction.createPurchase(itemId, purchaseId, quantity, unitCost));
        }else {
            throw  new EntityNotFoundException("Item doesn't exists");
        }
    }
}
