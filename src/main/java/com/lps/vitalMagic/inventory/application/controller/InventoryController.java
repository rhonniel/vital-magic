package com.lps.vitalMagic.inventory.application.controller;


import com.lps.vitalMagic.inventory.application.usecase.FindItemsWithLowStockUseCase;
import com.lps.vitalMagic.inventory.application.view.ItemInventoryView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final FindItemsWithLowStockUseCase findItemsWithLowStockUseCase;

    public InventoryController(FindItemsWithLowStockUseCase findItemsWithLowStockUseCase) {
        this.findItemsWithLowStockUseCase = findItemsWithLowStockUseCase;
    }

    @GetMapping("/low-stock")
    public List<ItemInventoryView> searchItem(){
        return findItemsWithLowStockUseCase.execute();
    }
}
