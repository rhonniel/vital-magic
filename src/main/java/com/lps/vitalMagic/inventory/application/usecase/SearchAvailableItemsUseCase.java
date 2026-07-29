package com.lps.vitalMagic.inventory.application.usecase;

import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.sales.application.pagination.PageResult;

public interface SearchAvailableItemsUseCase {
    PageResult<ItemView> execute(SearchItemsQuery query);
}
