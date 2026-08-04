package com.lps.vitalMagic.inventory.application.usecase;

import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.common.presentation.pagination.PageResult;

public interface SearchAvailableItemsUseCase {
    PageResult<ItemView> execute(SearchItemsQuery query);
}
