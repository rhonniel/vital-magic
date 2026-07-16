package com.lps.vitalMagic.purchase.application.service;

import com.lps.vitalMagic.inventory.application.dto.ItemInfo;
import com.lps.vitalMagic.inventory.application.service.FindItemService;
import com.lps.vitalMagic.inventory.application.service.RegisterPurchaseTransactionService;
import com.lps.vitalMagic.purchase.application.command.CreatePurchaseCommand;
import com.lps.vitalMagic.purchase.application.command.CreatePurchaseItemCommand;
import com.lps.vitalMagic.purchase.application.usecase.RegisterPurchaseUseCase;
import com.lps.vitalMagic.purchase.domain.model.entity.ItemSnapshot;
import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.domain.model.entity.PurchaseItem;
import com.lps.vitalMagic.purchase.domain.model.input.PurchaseItemInput;
import com.lps.vitalMagic.purchase.domain.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegisterPurchaseService implements RegisterPurchaseUseCase {
    private final FindItemService findItemService;
    private final RegisterPurchaseTransactionService registerPurchaseTransactionService;
    private final PurchaseRepository purchaseRepository;

    public RegisterPurchaseService(FindItemService findItemService, RegisterPurchaseTransactionService registerPurchaseTransactionService, PurchaseRepository purchaseRepository) {
        this.findItemService = findItemService;
        this.registerPurchaseTransactionService = registerPurchaseTransactionService;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    @Transactional
    public Long execute(CreatePurchaseCommand command) {

        List<PurchaseItemInput> purchaseItemsInputs= new ArrayList<>();

        for(CreatePurchaseItemCommand purchaseItemCommand:command.items()){
          ItemInfo itemInfo= findItemService.getItemInfo(purchaseItemCommand.itemId());

            ItemSnapshot itemSnapshot= new  ItemSnapshot(itemInfo.itemId(),itemInfo.name(),
                    purchaseItemCommand.unitCost());

            purchaseItemsInputs.add(new PurchaseItemInput(itemSnapshot,purchaseItemCommand.quantity()));

        }

       Purchase newPurchase= Purchase.create(purchaseItemsInputs);

        newPurchase=  purchaseRepository.save(newPurchase);

        for(PurchaseItem purchaseItem:newPurchase.getItems()){
            registerPurchaseTransactionService.registerPurchase( newPurchase.getId(),
                    purchaseItem.getItem().itemId(),purchaseItem.getQuantity(),purchaseItem.getItem().unitCost());

        }

        return newPurchase.getId();
    }
}
