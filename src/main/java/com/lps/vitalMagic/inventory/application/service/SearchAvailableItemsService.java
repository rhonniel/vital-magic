package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemAttributeView;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.inventory.application.usecase.SearchAvailableItemsUseCase;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import com.lps.vitalMagic.sales.application.pagination.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchAvailableItemsService implements SearchAvailableItemsUseCase {
    private final ItemRepository itemRepository;


    public SearchAvailableItemsService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public PageResult<ItemView> execute(SearchItemsQuery query) {

      return itemRepository.searchAvailableItems(query);


    }
}
