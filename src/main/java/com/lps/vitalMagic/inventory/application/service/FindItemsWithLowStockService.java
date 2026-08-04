package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.application.view.ItemInventoryView;
import com.lps.vitalMagic.inventory.application.usecase.FindItemsWithLowStockUseCase;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FindItemsWithLowStockService implements FindItemsWithLowStockUseCase {
    @Override
    public List<ItemInventoryView> execute() {
        return List.of();
    }
}
