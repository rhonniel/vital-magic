package com.lps.vitalMagic.inventory.domain.repository;

import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.common.presentation.pagination.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface ItemRepository {
    Optional<Item> findById(Long id);
    Item save(Item item);
    PageResult<ItemView> searchAvailableItems(SearchItemsQuery query);

    boolean existsById(Long id);

    List<Item> findAllById(Set<Long> itemIds);
}
