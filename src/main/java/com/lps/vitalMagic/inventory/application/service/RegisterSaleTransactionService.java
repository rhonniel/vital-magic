package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.domain.exception.InventoryTransactionException;
import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.domain.repository.InventoryTransactionRepository;
import org.springframework.stereotype.Service;


@Service
public class RegisterSaleTransactionService {

    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final ItemCurrentStockService itemCurrentStockService;

    public RegisterSaleTransactionService(InventoryTransactionRepository inventoryTransactionRepository,
                                          ItemCurrentStockService itemCurrentStockService) {
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.itemCurrentStockService =itemCurrentStockService;
    }

    public void registerInventoryTransaction(Long saleId, Long itemId, int quantity){

        if(itemCurrentStockService.getCurrentStock(itemId)>=quantity) {
           inventoryTransactionRepository.save(InventoryTransaction.createSale(itemId,saleId,quantity));
        }else {
            throw new InventoryTransactionException("The inventory don't have enough existences");
        }

    }
}
