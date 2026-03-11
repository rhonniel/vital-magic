package com.lps.vitalMagic.inventory.application.usecase;

import com.lps.vitalMagic.inventory.application.view.ItemInventoryView;

import java.util.List;

public interface FindItemsWithLowStockUseCase {
    List<ItemInventoryView> execute();
}
