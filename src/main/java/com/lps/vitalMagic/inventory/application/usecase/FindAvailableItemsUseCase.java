package com.lps.vitalMagic.inventory.application.usecase;

import com.lps.vitalMagic.inventory.application.query.FindAvailableItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemView;

import java.util.List;

public interface FindAvailableItemsUseCase {
    List<ItemView> execute(FindAvailableItemsQuery query);
}
