package com.lps.vitalMagic.inventory.application.usecase;

import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemView;

import java.util.List;

public interface SearchAvailableItemsUseCase {
    List<ItemView> execute(SearchItemsQuery query);
}
