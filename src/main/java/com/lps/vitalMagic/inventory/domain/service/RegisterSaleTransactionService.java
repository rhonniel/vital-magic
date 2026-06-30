package com.lps.vitalMagic.inventory.domain.service;

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


    //  la logica detras de mi desicion de hacer esto es, que veo mas apropiado dado la independdncia del metodo
    //  que el reciba lo queb el necesita de ahi en dalente el se las arregla, el metodo qu elo llame que le de loq ue el necesita

    public void registerInventoryTransaction(Long saleId, Long itemId, int quantity){

        if(itemCurrentStockService.getCurrentStock(itemId)>=quantity) {
           inventoryTransactionRepository.save(InventoryTransaction.createSale(itemId,saleId,quantity));
        }else {
            throw new InventoryTransactionException("The inventory don't have enough existences");
        }

    }
}
