package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.application.query.FindAvailableItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.inventory.application.usecase.FindAvailableItemsUseCase;
import java.util.List;

public class FindAvailableItemsService implements FindAvailableItemsUseCase {
    @Override
    public List<ItemView> execute(FindAvailableItemsQuery query) {
        return List.of();
    }
}
